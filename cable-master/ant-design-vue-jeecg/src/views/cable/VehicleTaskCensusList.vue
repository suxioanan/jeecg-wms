<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="年份">
              <a-select v-model="queryParam.taskTime" placeholder="请选择年份进行查询" @change="searchQuery">
                <a-select-option :value="year">{{year}}</a-select-option>
                <a-select-option :value="year-1">{{year-1}}</a-select-option>
                <a-select-option :value="year-2">{{year-2}}</a-select-option>
                <a-select-option :value="year-3">{{year-3}}</a-select-option>
                <a-select-option :value="year-4">{{year-4}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="人员">
              <a-select v-model="queryParam.realName" placeholder="请选择人员进行查询" @change="searchQuery">
                <a-select-option value="">全部</a-select-option>
                <template v-for="(user,index) in userList">
                  <a-select-option v-bind:value="user.realname">{{user.realname}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>

<!--          <a-col :md="6" :sm="10">-->
<!--            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">-->
<!--              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
<!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
<!--            </span>-->
<!--          </a-col>-->

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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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

      </a-table>
    </div>
    <!-- table区域-END -->

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import ACol from 'ant-design-vue/es/grid/Col'
  import {getAction} from '../../api/manage'

  export default {
    name: 'VehicleTaskCensusList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ACol,
      AFormItem,
    },
    data() {
      return {
        description: '人员任务统计',
        // 计划年份
        year: '',
        // 查询条件
        queryParam: {taskTime:'',realName:''},
        // 员工结果集
        userList: {},
        // 表头
        columns: [
          {
            title:'人员',
            align:"center",
            dataIndex: 'realName'
          },{
            title:'1月',
            align:"center",
            dataIndex: 'm1'
          },{
            title:'2月',
            align:"center",
            dataIndex: 'm2'
          },{
            title:'3月',
            align:"center",
            dataIndex: 'm3'
          },{
            title:'4月',
            align:"center",
            dataIndex: 'm4'
          },{
            title:'5月',
            align:"center",
            dataIndex: 'm5'
          },{
            title:'6月',
            align:"center",
            dataIndex: 'm6'
          },{
            title:'7月',
            align:"center",
            dataIndex: 'm7'
          },{
            title:'8月',
            align:"center",
            dataIndex: 'm8'
          },{
            title:'9月',
            align:"center",
            dataIndex: 'm9'
          },{
            title:'10月',
            align:"center",
            dataIndex: 'm10'
          },{
            title:'11月',
            align:"center",
            dataIndex: 'm11'
          },{
            title:'12月',
            align:"center",
            dataIndex: 'm12'
          },{
            title:'合计',
            align:"center",
            dataIndex: 'nums'
          }
        ],
        url: {
          list: '/cable/statisticalReport/list'
        }
      }
    },
    created() {
      this.getUsers();
      this.getCurrentYear();
    },
    methods: {
      // 查询所有用户数据并展示到搜索下拉框中
      getUsers() {
        getAction("/cable/statisticalReport/getUsers").then((resp) => {
          if (resp.success) {
            this.userList = resp.result;
          }
        });
      },
      // 计划年份
      getCurrentYear() {
        let date = new Date();
        this.year = date.getFullYear();
        this.queryParam.taskTime=this.year;
      }
    }
  }
</script>

<style scoped>

</style>