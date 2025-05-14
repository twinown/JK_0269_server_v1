# Получаем предсказания
y_pred = model.predict(proc_x_valid)

# Вычисляем confusion matrix
cm = confusion_matrix(y_test, y_pred)
TN, FP, FN, TP = cm.ravel()

# Строим таблицу с подписями
cm_labeled = pd.DataFrame(
    [[f'TP = {TP}', f'FP = {FP}'],
     [f'FN = {FN}', f'TN = {TN}']],
    index=['y_pred = 1', 'y_pred = 0'],
    columns=['y_real = 1', 'y_real = 0']
)

# Показываем
print(cm_labeled)

cm_df

