<template>
  <div id="uploadDataPage">
    <h1 class="title">上传数据</h1>
    <div class="notice-box">
      注意：<br>
      1. 请确保上传的 CSV 文件格式正确，否则可能导致数据导入失败。<br>
      2. 数据列应包含以下字段：<br>
      - 时间 : data_time（YYYY-MM-DD HH:mm:ss）<br>
      - 年-year、月-month、日-day、小时-hour、温度-temperature、露点温度-dew_point<br>
      - 气压-pressure、风向-wind_direction、风速-wind_speed
    </div>

    <a-spin :spinning="isUploading" tip="数据正在上传导入Hive数据仓库中，请稍候..." size="large">
      <div class="upload-container">
        <a-form :model="formState" layout="vertical">
          <a-form-item
              label="选择年份"
              name="year"
              :rules="[{ required: true, message: '请选择年份' }]"
          >
            <a-date-picker
                v-model:value="formState.year"
                picker="year"
                placeholder="请选择年份"
                style="width: 100%"
                :disabled="isUploading"
            />
          </a-form-item>

          <a-upload-dragger
              name="file"
              :multiple="false"
              :custom-request="customRequest"
              :before-upload="beforeUpload"
              @change="handleChange"
              :show-upload-list="false"
              :disabled="!formState.year || isUploading"
          >
            <p class="ant-upload-drag-icon">
              <inbox-outlined></inbox-outlined>
            </p>
            <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
            <p class="ant-upload-hint">
              仅支持上传 CSV 文件
            </p>
          </a-upload-dragger>

          <div v-if="uploadProgress > 0" class="progress-container">
            <a-progress :percent="uploadProgress" :status="uploadStatus"/>
            <p v-if="isUploading" class="uploading-text">
              <loading-outlined/>
              数据正在导入Hive数据仓库，这可能需要一些时间...
            </p>
          </div>

          <div v-if="uploadStatus === 'success'" class="success-container">
            <a-result
                status="success"
                title="文件上传成功"
                sub-title="数据已成功导入Hive数据仓库"
            >
              <template #extra>
                <a-button type="primary" @click="resetUpload">
                  继续上传
                </a-button>
              </template>
            </a-result>
          </div>
        </a-form>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive} from 'vue'
import {message} from 'ant-design-vue'
import {InboxOutlined, LoadingOutlined} from '@ant-design/icons-vue'
import {uploadAndImportUsingPost} from '@/api/fileController'
import dayjs from 'dayjs'

interface FormState {
  year: dayjs.Dayjs | null;
}

const formState = reactive<FormState>({
  year: null
})

const uploadProgress = ref(0)
const uploadStatus = ref('')
const isUploading = ref(false) // 新增上传状态标志

const beforeUpload = (file: File) => {
  if (!formState.year) {
    message.error('请先选择年份！')
    return false
  }
  const isCSV = file.type === 'text/csv' || file.name.endsWith('.csv')
  if (!isCSV) {
    message.error('只能上传 CSV 文件！')
    return false
  }
  return true
}

const customRequest = async (options: any) => {
  const {file, onProgress, onSuccess, onError} = options
  isUploading.value = true // 开始上传

  try {
    const year = formState.year?.format('YYYY')
    const response = await uploadAndImportUsingPost(
        {year: year},
        {},
        file
    )

    if (response.data.code === 0) {
      onSuccess(response)
    } else {
      message.error(response.data.message)
      onError(new Error(response.data.message || '上传失败'))
    }
  } catch (error) {
    console.error('上传失败:', error)
    message.error('上传失败，请检查网络连接或文件格式')
    onError(error)
  } finally {
    isUploading.value = false // 上传结束
  }
}

const handleChange = (info: any) => {
  const status = info.file.status
  if (status === 'uploading') {
    uploadProgress.value = info.file.percent
    uploadStatus.value = 'active'
  } else if (status === 'done') {
    uploadProgress.value = 100
    uploadStatus.value = 'success'
    message.success('文件上传成功！数据正在导入Hive数据仓库...')
  } else if (status === 'error') {
    uploadStatus.value = 'exception'
    message.error('文件上传失败，请重试！')
  }
}

const resetUpload = () => {
  uploadProgress.value = 0
  uploadStatus.value = ''
  formState.year = null
}
</script>

<style scoped>
#uploadDataPage {
  padding: 20px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.notice-box {
  background-color: #f2f9ff;
  color: #666;
  padding: 15px;
  margin: 0 auto 20px;
  max-width: 800px;
  border-radius: 4px;
  text-align: left;
  line-height: 1.6;
  border: 1px solid #d9e7fd;
}

.upload-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.progress-container {
  margin-top: 20px;
  text-align: center;
}

.uploading-text {
  margin-top: 10px;
  color: #1890ff;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.success-container {
  margin-top: 20px;
  padding: 20px;
  background-color: #f6ffed;
  border-radius: 4px;
  border: 1px solid #b7eb8f;
}

:deep(.ant-spin-blur) {
  opacity: 0.5;
}
</style>