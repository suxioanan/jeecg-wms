<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="5" :sm="7">
            <a-form-item label="物料编号">
              <a-input v-model="queryParam.serial" placeholder="请输入物料编号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="5" :sm="7">
            <a-form-item label="物料名称">
              <a-input v-model="queryParam.name" placeholder="请输入物料名称"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="5" :sm="7">
            <a-form-item label="项目编号">
              <a-input v-model="queryParam.projectNo" placeholder="请输入项目编号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="7" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
             <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

          <!--<a-dropdown v-if="selectedRowKeys.length > 0">
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="batchDel">
                <a-icon type="delete"/>
                删除
              </a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px;font-weight: bold;"> 批量操作
              <a-icon type="down"/>
            </a-button>
          </a-dropdown>-->

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      rowKey="id"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"-->
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>

        <!-- 自定义表头标题-begin -->
        <span slot="year4">{{this.year-4}}</span>
        <span slot="year3">{{this.year-3}}</span>
        <span slot="year2">{{this.year-2}}</span>
        <span slot="year1">{{this.year-1}}</span>
        <span slot="year">{{this.year}}</span>  <!-- 表头计算年份(动态展示当前年份) -->
        <!-- 自定义表头标题-END -->

      </a-table>
    </div>
    <!-- table区域-END -->

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import ACol from 'ant-design-vue/es/grid/Col'
  import JEllipsis from '../../components/jeecg/JEllipsis'

  export default {
    name: 'MaterialRemainingAccount',
    mixins: [JeecgListMixin],
    components: {
      ACol,
      AFormItem,
      JEllipsis
    },
    data() {
      return {
        // 组件说明
        description: '物料余留台账',
        // 计划年份
        year: '',
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            title: '物料编号',
            align: 'center',
            dataIndex: 'serial',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            title: '物料名称',
            align: 'center',
            dataIndex: 'name',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            align: 'center',
            slots: { title: 'year4' },
            children: [
              {
                title: '余留出',
                align: 'center',
                dataIndex: 'ylck4'
              }, {
                title: '余留入',
                align: 'center',
                dataIndex: 'ylrk4'
              }
            ]
          }, {
            align: 'center',
            slots: { title: 'year3' },
            children: [
              {
                title: '余留出',
                align: 'center',
                dataIndex: 'ylck3'
              }, {
                title: '余留入',
                align: 'center',
                dataIndex: 'ylrk3'
              }
            ]
          }, {
            align: 'center',
            slots: { title: 'year2' },
            children: [
              {
                title: '余留出',
                align: 'center',
                dataIndex: 'ylck2'
              }, {
                title: '余留入',
                align: 'center',
                dataIndex: 'ylrk2'
              }
            ]
          }, {
            align: 'center',
            slots: { title: 'year1' },
            children: [
              {
                title: '余留出',
                align: 'center',
                dataIndex: 'ylck1'
              }, {
                title: '余留入',
                align: 'center',
                dataIndex: 'ylrk1'
              }
            ]
          }, {
            align: 'center',
            slots: { title: 'year' },
            children: [
              {
                title: '余留出',
                align: 'center',
                dataIndex: 'ylck'
              }, {
                title: '余留入',
                align: 'center',
                dataIndex: 'ylrk'
              }
            ]
          }, {
            title: '合计',
            align: 'center',
            children: [
              {
                title: '余留出',
                align: 'center',
                dataIndex: 'ylcknums'
              }, {
                title: '余留入',
                align: 'center',
                dataIndex: 'ylrknums'
              }
            ]
          }
        ],
        // 访问路径
        url: {
          list: '/cable/material/getMaterialRemainingAccountList'
        }
      }
    },
    methods: {
      // 计划年份
      getCurrentYear() {
        let date = new Date()
        this.year = date.getFullYear()
      }
    },
    mounted() {
      // 调用计划年份
      this.getCurrentYear()
    }
  }
</script>

<style scoped>

</style>