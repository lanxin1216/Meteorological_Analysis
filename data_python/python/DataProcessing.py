import pandas as pd
import numpy as np

# 还需要安装模块 “ openpyxl” 》 命令: pip install openpyxl

# 读取数据
# data = pd.read_table('./data/509530-99999-2021', header=None)
# data = pd.read_table('./data/509530-99999-2022', header=None)
# data = pd.read_table('./data/509530-99999-2023', header=None)
data = pd.read_table('./data/509530-99999-2024', header=None)

'''
原始数据中以空格分隔的12列数据，分别为：
年、月、日、小时、温度、露点温度、气压、风向、风速、云量、1小时降雨量和6小时降雨量。
'''
# 构建空列表用于存放提取出来的各列数据
data_list = []
for line in data.values:
    line_temp = [int(x) for x in line[0].split(' ') if x != '']
    data_list.append(line_temp)

df = pd.DataFrame(data_list,
                  columns=['year', 'month', 'day', 'hour', 'temperature', 'dew_point', 'pressure', 'wind_direction',
                           'wind_speed', 'cloud_cover', 'one_hour_rainfall', 'six_hour_rainfall'])

# 对数据中-9999的缺失值进行NaN替换
df = df.replace(-9999, np.nan)

# 数据说明文档中表示原始数据中温度、露点温度、气压、风速、降雨量的换算系数为10，所以要对原始数据中的对应数据除以10，进行换算。
df['temperature'] = df['temperature'] / 10
df['dew_point'] = df['dew_point'] / 10
df['pressure'] = df['pressure'] / 10
df['wind_speed'] = df['wind_speed'] / 10
df['one_hour_rainfall'] = df['one_hour_rainfall'] / 10
df['six_hour_rainfall'] = df['six_hour_rainfall'] / 10

# 清洗 - 云量、1小时降雨量和6小时降雨量大部分都是空，直接删除
df.drop(columns=['cloud_cover', 'one_hour_rainfall', 'six_hour_rainfall'], inplace=True)

# 给数据增加一个DataFrame列
df['data_time'] = pd.to_datetime(df[['year', 'month', 'day', 'hour']], format='%Y %m %d %H').dt.strftime('%Y-%m-%d %H:%M:%S.%f')
# 设置索引
df = df.set_index(df['data_time'])
df.drop(columns='data_time', inplace=True)
print(df)

# 保存为同名excel和csv
# df.to_excel('./data/new/592870-99999-2021_xlsx.xlsx')
# df.to_csv('./data/new/592870-99999-2021_csv.csv')

# df.to_excel('./data/new/592870-99999-2022_xlsx.xlsx')
# df.to_csv('./data/new/592870-99999-2022_csv.csv')
#
# df.to_excel('./data/new/592870-99999-2023_xlsx.xlsx')
# df.to_csv('./data/new/592870-99999-2023_csv.csv')
# #
df.to_excel('./data/new/592870-99999-2024_xlsx.xlsx')
df.to_csv('./data/new/592870-99999-2024_csv.csv')
