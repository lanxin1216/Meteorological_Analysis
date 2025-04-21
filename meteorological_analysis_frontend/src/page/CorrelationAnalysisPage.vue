<template>
  <div id="correlationAnalysis">
    <h1 class="page-title">气象相关性分析</h1>
    <div class="input-container">
      <a-space>
        <div>请选择年份: </div>
        <a-date-picker
            v-model:value="selectedYear"
            picker="year"
            placeholder="选择年份"
            style="width: 200px"
        />
      </a-space>
    </div>
    <!-- 图表组件 -->
    <TempPressureChart :year="selectedYear"/>
    <DewPointDistributionChart :year="selectedYear"/>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import TempPressureChart from "@/components/correlationAnalysis/TempPressureChart.vue";
import DewPointDistributionChart from "@/components/correlationAnalysis/DewPointDistributionChart.vue";
import dayjs from "dayjs";

const selectedYear = ref(dayjs())

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
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
