<template>
  <div id="homePage">
    <a-layout-header class="header">
      <a-tabs v-model:activeKey="current" @change="handleTabChange">
        <a-tab-pane :key="item.key" v-for="item in originItems" :tab="item.label"/>
      </a-tabs>
    </a-layout-header>

    <!-- 动态加载的内容 -->
    <div class="content">
      <component :is="currentComponent"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router'
import {computed, ref, watch} from 'vue'
import BasicAnalysisPage from "@/page/BasicAnalysisPage.vue";
import ExtremeAnalysisPage from "@/page/ExtremeAnalysisPage.vue";
import CorrelationAnalysisPage from "@/page/CorrelationAnalysisPage.vue";
import TrendAnalysisPage from "@/page/TrendAnalysisPage.vue";

defineOptions({
  name: 'adminSystemManagePage',
})

// 菜单列表
const originItems = [
  {
    key: 'basicAnalysis',
    label: '基础分析',
    component: BasicAnalysisPage,
  },
  {
    key: 'extremeAnalysis',
    label: '极端分析',
    component: ExtremeAnalysisPage,
  },
  {
    key: 'trendAnalysis',
    label: '趋势分析',
    component: TrendAnalysisPage,

  },
  {
    key: 'correlationAnalysis',
    label: '相关性分析',
    component: CorrelationAnalysisPage,
  }
]

const route = useRoute()
const router = useRouter()
// 当前选中的菜单
const current = ref<string>(route.query.tag?.toString() || originItems[0].key)

// 当前组件
const currentComponent = computed(() => {
  const item = originItems.find((item) => item.key === current.value)
  return item ? item.component : null
})

// 监听 URL 参数变化
watch(
    () => route.query.tag,
    (newTag) => {
      if (newTag) {
        current.value = newTag.toString()
      }
    },
)

// 切换 Tab 时更新 URL
const handleTabChange = (key: string) => {
  router.replace({query: {tag: key}})
}
</script>

<style scoped>
#homePage .header {
  height: 46px;
  background-color: white;
  padding: 0 24px;
}

#homePage .content {
  padding: 16px 24px;
  background-color: white;
}
</style>
