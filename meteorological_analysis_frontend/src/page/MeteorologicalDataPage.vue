<template>
  <div id="meteorologicalDataPage">
    <div class="title">
      <h1 class="title-text">历史气象数据</h1>
    </div>

    <div class="search-container">
      <a-form :model="searchForm" layout="inline">
        <a-form-item label="选择年份">
          <a-date-picker
              v-model:value="searchForm.year"
              picker="year"
              placeholder="请选择年份"
              style="width: 200px"
          />
        </a-form-item>

        <a-form-item>
          <a-space>
            <a-button type="primary" @click="handleSearch" :loading="loading">
              搜索
            </a-button>
            <a-button @click="handleReset">
              重置
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>

    <a-alert class="info"
             message="注意：仅展示每年的前500条数据。"
             show-icon
             type="warning"/>

    <div class="table-container">
      <a-table
          :columns="columns"
          :data-source="dataSource"
          :loading="loading"
          :scroll="{ y: 'calc(100vh - 300px)' }"
          bordered
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'data_time'">
            {{ formatDateTime(record.data_time) }}
          </template>
          <template v-if="column.dataIndex === 'temperature'">
            {{ formatValue(record.temperature) }}°C
          </template>
          <template v-if="column.dataIndex === 'dew_point'">
            {{ formatValue(record.dew_point) }}°C
          </template>
          <template v-if="column.dataIndex === 'pressure'">
            {{ formatValue(record.pressure) }} hPa
          </template>
          <template v-if="column.dataIndex === 'wind_direction'">
            {{ formatValue(record.wind_direction) }}°
          </template>
          <template v-if="column.dataIndex === 'wind_speed'">
            {{ formatValue(record.wind_speed) }} m/s
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive} from 'vue'
import {message} from 'ant-design-vue'
import dayjs from 'dayjs'
import {getTop500ByYearUsingGet} from "@/api/weatherBasicAnalysisController.ts";

interface SearchForm {
  year: dayjs.Dayjs | null;
}

interface WeatherData {
  data_time: string;
  temperature: number;
  dew_point: number;
  pressure: number;
  wind_direction: number;
  wind_speed: number;
  year: number;
  month: number;
  day: number;
  hour: number;
}

const columns = [
  {
    title: '时间',
    dataIndex: 'data_time',
    width: 180,
    fixed: 'left'
  },
  {
    title: '温度',
    dataIndex: 'temperature',
    width: 120,
  },
  {
    title: '露点温度',
    dataIndex: 'dew_point',
    width: 120,
  },
  {
    title: '气压',
    dataIndex: 'pressure',
    width: 120,
  },
  {
    title: '风向',
    dataIndex: 'wind_direction',
    width: 120,
  },
  {
    title: '风速',
    dataIndex: 'wind_speed',
    width: 120,
  },
  {
    title: '年',
    dataIndex: 'year',
    width: 100,
  },
  {
    title: '月',
    dataIndex: 'month',
    width: 80,
  },
  {
    title: '日',
    dataIndex: 'day',
    width: 80,
  },
  {
    title: '时',
    dataIndex: 'hour',
    width: 80,
  },
]

const searchForm = reactive<SearchForm>({
  year: dayjs()
})

const loading = ref(false)
const dataSource = ref<WeatherData[]>([])

const formatDateTime = (timestamp: string) => {
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
}

const formatValue = (value: any): string => {
  if (value === null || value === undefined) return 'N/A';

  const numValue = Number(value);
  return isNaN(numValue) ? 'N/A' : numValue.toFixed(2);
};

const transformData = (rawData: any[]): WeatherData[] => {
  return rawData.map(item => {
    return {
      data_time: item.data_time || item.dataTime || '',
      temperature: parseFloat(item.temperature) || 0,
      dew_point: parseFloat(item.dew_point || item.dewPoint) || 0,
      pressure: parseFloat(item.pressure) || 0,
      wind_direction: parseFloat(item.wind_direction || item.windDirection) || 0,
      wind_speed: parseFloat(item.wind_speed || item.windSpeed) || 0,
      year: parseInt(item.year) || 0,
      month: parseInt(item.month) || 0,
      day: parseInt(item.day) || 0,
      hour: parseInt(item.hour) || 0
    };
  });
};

const fetchData = async () => {
  if (!searchForm.year) {
    message.warning('请选择年份')
    return
  }

  loading.value = true
  try {
    const year = searchForm.year.format('YYYY')
    const response = await getTop500ByYearUsingGet({year: year})

    if (response.data.code === 0) {
      dataSource.value = transformData(response.data.data)
    } else {
      message.error(response.data.message || '获取数据失败')
      dataSource.value = []
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    message.error('获取数据失败')
    dataSource.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchData()
}

const handleReset = () => {
  searchForm.year = dayjs()
  dataSource.value = []
}
</script>

<style scoped>
#meteorologicalDataPage {
  padding: 20px;
}

#meteorologicalDataPage .title {
  text-align: center;
  margin-bottom: 16px;
}

#meteorologicalDataPage .title-text {
  margin-bottom: 8px;
  font-size: 24px;
  color: rgba(0, 0, 0, 0.85);
}

#meteorologicalDataPage .search-container {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

#meteorologicalDataPage .table-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 调整表格高度 */
:deep(.ant-table-container) {
  max-height: calc(100vh - 300px);
  overflow-y: auto;
}

/* 固定表头 */
:deep(.ant-table) {
  position: relative;
}

:deep(.ant-table-thead) {
  position: sticky;
  top: 0;
  z-index: 1;
  background: white;
}
</style>