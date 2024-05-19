import torch
import torch.nn as nn
import numpy as np
from PIL import Image

from models.module_pixel_effect import PixelEffectModule


class Photo2PixelModel(nn.Module):
    def __init__(self):
        super(Photo2PixelModel, self).__init__()
        self.module_pixel_effect = PixelEffectModule()

    def forward(self, rgb,
                param_kernel_size=20,
                param_pixel_size=16,
                param_with_padding = 1):
        rgb = self.module_pixel_effect(rgb, 4, param_kernel_size, param_pixel_size,param_with_padding)
        return rgb
