<template>
  <div id="correlationAnalysis">
    <h1 class="page-title">气象相关性分析</h1>
    <a-space direction="vertical" size="large" style="width: 100%">
      <div class="input-container">
        <a-date-picker
            v-model:value="selectedYear"
            picker="year"
            placeholder="请选择年份"
            style="width: 200px"
        />
      </div>

      <!-- 图表组件 -->
      <TempPressureChart :year="selectedYear"/>
      <DewPointDistributionChart :year="selectedYear"/>
    </a-space>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted} from 'vue'
import TempPressureChart from "@/components/correlationAnalysis/TempPressureChart.vue";
import DewPointDistributionChart from "@/components/correlationAnalysis/DewPointDistributionChart.vue";

const selectedYear = ref<number | null>(null)

onMounted(() => {
  selectedYear.value = new Date().getFullYear()
})
</script>

<style scoped>
#correlationAnalysis {
  padding: 20px;
}

#correlationAnalysis .page-title {
  text-align: center;
  margin-bottom: 24px;
}

#correlationAnalysis .input-container {
  margin-bottom: 16px;
}
</style>
