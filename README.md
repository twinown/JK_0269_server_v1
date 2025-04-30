import streamlit as st
from PIL import Image
import numpy as np
import matplotlib.pyplot as plt
import skimage

st.title('Изменение четкости изображения')

uploaded_file = st.file_uploader("Выберите изображение", type=["jpg", "png", "jpeg"])
if uploaded_file is not None:
      image = Image.open(uploaded_file)
      st.image(image, caption="Загруженное изображение", use_column_width=True)
      image = skimage.color.rgb2gray(image)
      U, sing_values, V = np.linalg.svd(image)
      U.shape, V.shape, sing_values.shape
      S = np.zeros(shape=image.shape)
      np.fill_diagonal(S, sing_values)
      M = U@S@V
      plt.imshow(M, cmap='grey')
      top_k = st.slider(
            "Количество сингулярных чисел")
      trunc_U = U[:, :top_k]
      trunc_S = S[:top_k, :top_k]
      trunc_V = V[:top_k, :]
      trunc_M = trunc_U@trunc_S@trunc_V
          # Отображение результатов
      fig, ax = plt.subplots(1, 2, figsize=(10, 5))
      ax[0].imshow(image, cmap='gray')
      ax[0].set_title('Оригинал (grayscale)')
      ax[0].axis('off')
    
      ax[1].imshow(trunc_M, cmap='gray')
      ax[1].set_title(f'Сжатое (top {top_k} компонент)')
      ax[1].axis('off')

