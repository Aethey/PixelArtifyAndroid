import torch
import torch.nn as nn
import torch.nn.functional as F
import numpy as np
from PIL import Image


class PixelEffectModule(nn.Module):
    def __init__(self):
        super(PixelEffectModule, self).__init__()

    def create_mask_by_idx(self, idx_z, max_z):
        """
        :param idx_z: [H, W]
        :return:
        """
        h, w = idx_z.shape
        idx_x = torch.arange(h).view([h, 1]).repeat([1, w])
        idx_y = torch.arange(w).view([1, w]).repeat([h, 1])
        mask = torch.zeros([h, w, max_z])
        mask[idx_x, idx_y, idx_z] = 1
        return mask

    def select_by_idx(self, data, idx_z):
        """
        :param data: [h,w,C]
        :param idx_z: [h,w]
        :return:
        """
        h, w = idx_z.shape
        idx_x = torch.arange(h).view([h, 1]).repeat([1, w])
        idx_y = torch.arange(w).view([1, w]).repeat([h, 1])
        return data[idx_x, idx_y, idx_z]

    def forward(self, rgb, param_num_bins, param_kernel_size, param_pixel_size,param_with_padding):
        """
        :param rgb:[b(1), c(3), H, W]
        :return: [b(1), c(3), H, W]
        """

        r, g, b = rgb[:, 0:1, :, :], rgb[:, 1:2, :, :], rgb[:, 2:3, :, :]

        intensity_idx = torch.mean(rgb, dim=[0, 1]) / 256. * param_num_bins
        intensity_idx = intensity_idx.long()

        intensity = self.create_mask_by_idx(intensity_idx, max_z=param_num_bins)
        intensity = intensity.permute(dims=[2, 0, 1]).unsqueeze(dim=0)

        r, g, b = r * intensity, g * intensity, b * intensity

        kernel_conv = torch.ones([param_num_bins, 1, param_kernel_size, param_kernel_size])
        r = F.conv2d(input=r, weight=kernel_conv, padding=(param_kernel_size - 1) // 2, stride=param_pixel_size, groups=param_num_bins, bias=None)[0, :, :, :]
        g = F.conv2d(input=g, weight=kernel_conv, padding=(param_kernel_size - 1) // 2, stride=param_pixel_size, groups=param_num_bins, bias=None)[0, :, :, :]
        b = F.conv2d(input=b, weight=kernel_conv, padding=(param_kernel_size - 1) // 2, stride=param_pixel_size, groups=param_num_bins, bias=None)[0, :, :, :]
        intensity = F.conv2d(input=intensity, weight=kernel_conv, padding=(param_kernel_size - 1) // 2, stride=param_pixel_size, groups=param_num_bins,
                             bias=None)[0, :, :, :]
        intensity_max, intensity_argmax = torch.max(intensity, dim=0)


        r = r.permute(dims=[1, 2, 0])
        g = g.permute(dims=[1, 2, 0])
        b = b.permute(dims=[1, 2, 0])

        r = self.select_by_idx(r, intensity_argmax)
        g = self.select_by_idx(g, intensity_argmax)
        b = self.select_by_idx(b, intensity_argmax)

        r = r / intensity_max
        g = g / intensity_max
        b = b / intensity_max

        result_rgb = torch.stack([r, g, b], dim=-1)
        result_rgb = result_rgb.permute(dims=[2, 0, 1]).unsqueeze(dim=0)
        result_rgb = F.interpolate(result_rgb, scale_factor=param_pixel_size)
        
        # Apply padding by add for loop
        if param_with_padding == 1:
            padding_size = 1
            b, c, h, w = result_rgb.shape
            padded_result_rgb = torch.zeros((b, c, h + (h // param_pixel_size) * padding_size, w + (w // param_pixel_size) * padding_size), device=rgb.device)
            
            for i in range(h // param_pixel_size):
                for j in range(w // param_pixel_size):
                    start_h = i * (param_pixel_size + padding_size)
                    start_w = j * (param_pixel_size + padding_size)
                    
                    if i == 0:
                        start_h = i * param_pixel_size  # No top padding for the first row
                    if j == 0:
                        start_w = j * param_pixel_size  # No left padding for the first column
                    if i == (h // param_pixel_size - 1):
                        start_h = i * (param_pixel_size + padding_size)  # No bottom padding for the last row
                    if j == (w // param_pixel_size - 1):
                        start_w = j * (param_pixel_size + padding_size)  # No right padding for the last column

                    padded_result_rgb[:, :, start_h:start_h + param_pixel_size, start_w:start_w + param_pixel_size] = result_rgb[:, :, i * param_pixel_size:(i + 1) * param_pixel_size, j * param_pixel_size:(j + 1) * param_pixel_size]

            return padded_result_rgb
        else:
            return result_rgb

 