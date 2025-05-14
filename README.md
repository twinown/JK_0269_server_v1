Постройте __confusion matrix__ на тестовой выборке. Отобразите ее в виде DataFrame

|          | y_real = 1  | y_real = 0  |
|----------|------------|------------|
|  y_pred = 1  |     TP     |     FP     |
|  y_pred = 0  |     FN     |     TN     |  

y_pred = model.predict(proc_x_valid)

cm = confusion_matrix(y_test, y_pred)

# Создаем DataFrame с нужной структурой
cm_df = pd.DataFrame([cm[1,1], cm[0,1]],  # TP, FP
    [['y_pred = 1'], ['y_pred = 0']],  # Индексы строк
    columns=['y_real = 1', 'y_real = 0']  # Названия столбцов
)

# Переименовываем ячейки согласно вашей схеме
cm_df = cm_df.rename(index={
    'y_pred = 1': 'TP/FP',
    'y_pred = 0': 'FN/TN'
})
