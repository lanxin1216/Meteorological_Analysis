<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader/>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view/>
      </a-layout-content>
      <a-layout-footer class="footer">哈尔滨市&气象历史数据分析平台</a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/layouts/GlobalHeader.vue";
</script>

<style scoped>
#basicLayout .header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding-inline: 20px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

#basicLayout .content {
  margin-top: 64px;
  background: linear-gradient(to right, #fefefe, #fff);
  min-height: calc(100vh - 64px);
}

#basicLayout .footer {
  background: #efefef;
  padding: 6px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>