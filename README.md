Постройте __confusion matrix__ на тестовой выборке. Отобразите ее в виде DataFrame

|          | y_real = 1  | y_real = 0  |
|----------|------------|------------|
|  y_pred = 1  |     TP     |     FP     |
|  y_pred = 0  |     FN     |     TN     |  

# Получаем предсказания
y_pred = model.predict(proc_x_valid)

# Вычисляем confusion matrix
cm = confusion_matrix(y_test, y_pred)

# Создаём DataFrame с понятными подписями
cm_df = pd.DataFrame(
    [[cm[1,1], cm[1,0]],   # [TP, FN]
     [cm[0,1], cm[0,0]]],  # [FP, TN]
    index=['y_pred = 1', 'y_pred = 0'],
    columns=['y_real = 1', 'y_real = 0']
)

cm_df

