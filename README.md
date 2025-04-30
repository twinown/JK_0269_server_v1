import streamlit as st
from PIL import Image
import numpy as np
import matplotlib.pyplot as plt
from skimage.color import rgb2gray

st.title('Изменение чёткости изображения с помощью SVD')

uploaded_file = st.file_uploader("Загрузите изображение", type=["jpg", "jpeg", "png"])

if uploaded_file is not None:
    # Открываем и отображаем оригинал
    image_pil = Image.open(uploaded_file).convert("RGB")
    st.image(image_pil, caption="Загруженное изображение", use_column_width=True)

    # Переводим в numpy и серый формат
    image_np = np.array(image_pil)
    image_gray = rgb2gray(image_np)

    # SVD-разложение
    U, sing_values, Vt = np.linalg.svd(image_gray, full_matrices=False)

    # Слайдер для выбора числа компонент
    top_k = st.slider("Количество сингулярных чисел", min_value=5, max_value=min(U.shape), value=50)

    # Усечённая реконструкция
    trunc_U = U[:, :top_k]
    trunc_S = np.diag(sing_values[:top_k])
    trunc_V = Vt[:top_k, :]
    truncated_image = trunc_U @ trunc_S @ trunc_V

    # График до и после
    fig, ax = plt.subplots(1, 2, figsize=(10, 5))
    ax[0].imshow(image_gray, cmap='gray')
    ax[0].set_title('Оригинал (grayscale)')
    ax[0].axis('off')

    ax[1].imshow(truncated_image, cmap='gray')
    ax[1].set_title(f'Сжатое изображение (top {top_k})')
    ax[1].axis('off')

    st.pyplot(fig)


