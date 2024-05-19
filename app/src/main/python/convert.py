import torch
from PIL import Image
import argparse
from io import BytesIO

from models.module_photo2pixel import Photo2PixelModel
from utils import img_common_util



def convert(file_input,file_out_path,kernel_size,pixel_size,with_padding):

    img_input = Image.open(file_input)
    img_pt_input = img_common_util.convert_image_to_tensor(img_input)

    model = Photo2PixelModel()
    model.eval()
    with torch.no_grad():
        img_pt_output = model(
            img_pt_input,
            param_kernel_size=kernel_size,
            param_pixel_size=pixel_size,
            param_with_padding = with_padding
        )
    img_output = img_common_util.convert_tensor_to_image(img_pt_output)
    img_output.save(file_out_path)
    return file_out_path
 