почему у меня не сходится с ttest_ind
def ttest(sample_1, sample_2, alternative='two-sided', alpha=.05, independent=True):
    t_stat_distr = st.t(len(sample_1)+len(sample_2)-2)
    se11 = sample_1.var() / len(sample_1)
    se22 = sample_2.var() / len(sample_2)
    t_stat = (abs(sample_2.mean()-sample_1.mean()))/np.sqrt(se11+se22)
    #p_value = 2 * (1-t_stat_distr.cdf(abs(t_stat))) 
    
    if alternative == 'two-sided':
        p_value = 2 * (1-t_stat_distr.cdf(abs(t_stat)))
    
    if alternative == 'left':
        p_value = t_stat_distr.cdf(t_stat)
    
    if alternative == 'right':
        p_value = 1 - t_stat_distr.cdf(t_stat) 
    

    return t_stat, p_value
