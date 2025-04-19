<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="400px">
        <RouterLink to="/">
          <div class="title-bar">
            <div class="title">哈尔滨市&历史气象数据分析平台</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto">
        <a-menu
            v-model:selectedKeys="current"
            :items="items"
            mode="horizontal"
            @click="doMenuClick"
        />
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import {h, ref} from 'vue'
import {LineChartOutlined, UploadOutlined, SmileOutlined, GoldFilled} from '@ant-design/icons-vue'
import {type MenuProps} from 'ant-design-vue'
import {useRouter} from 'vue-router'

const current = ref<string[]>(['home'])
const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(LineChartOutlined),
    label: '数据分析',
    title: '数据分析',
  },
  {
    key: '/dataView',
    icon: () => h(GoldFilled),
    label: '气象数据',
    title: '气象数据',
  },
  {
    key: '/uploadData',
    icon: () => h(UploadOutlined),
    label: '上传数据',
    title: '上传数据',
  }
])

const router = useRouter();
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path];
});

// 路由跳转事件
const doMenuClick = ({key}: { key: string }) => {
  router.push({
    path: key,
  });
};

</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}
</style>
