<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin-->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="年份">
              <a-select placeholder="请选择年份进行查询" v-model="queryParam.taskTime" @change="searchQuery">
                <a-select-option :value="year">{{year}}</a-select-option>
                <a-select-option :value="year-1">{{year-1}}</a-select-option>
                <a-select-option :value="year-2">{{year-2}}</a-select-option>
                <a-select-option :value="year-3">{{year-3}}</a-select-option>
                <a-select-option :value="year-4">{{year-4}}</a-select-option>
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

        <span slot="m1" slot-scope="text, record"><a @click="showTaskView(record,1)">{{record.m1}}</a></span>
        <span slot="m2" slot-scope="text, record"><a @click="showTaskView(record,2)">{{record.m2}}</a></span>
        <span slot="m3" slot-scope="text, record"><a @click="showTaskView(record,3)">{{record.m3}}</a></span>
        <span slot="m4" slot-scope="text, record"><a @click="showTaskView(record,4)">{{record.m4}}</a></span>
        <span slot="m5" slot-scope="text, record"><a @click="showTaskView(record,5)">{{record.m5}}</a></span>
        <span slot="m6" slot-scope="text, record"><a @click="showTaskView(record,6)">{{record.m6}}</a></span>
        <span slot="m7" slot-scope="text, record"><a @click="showTaskView(record,7)">{{record.m7}}</a></span>
        <span slot="m8" slot-scope="text, record"><a @click="showTaskView(record,8)">{{record.m8}}</a></span>
        <span slot="m9" slot-scope="text, record"><a @click="showTaskView(record,9)">{{record.m9}}</a></span>
        <span slot="m10" slot-scope="text, record"><a @click="showTaskView(record,10)">{{record.m10}}</a></span>
        <span slot="m11" slot-scope="text, record"><a @click="showTaskView(record,11)">{{record.m11}}</a></span>
        <span slot="m12" slot-scope="text, record"><a @click="showTaskView(record,12)">{{record.m12}}</a></span>

      </a-table>
    </div>
    <!-- table区域-END -->

    <!-- 引入自定义组件[作用：显示车辆任务详细信息] -->
    <SysUserTaskCensusListModal ref="SysUserTaskCensusListModal"></SysUserTaskCensusListModal>

  </a-card>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import ACol from 'ant-design-vue/es/grid/Col'
  import SysUserTaskCensusListModal from './modules/SysUserTaskCensusListModal'

  export default {
    name: 'SysUserTaskCensusList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ACol,
      AFormItem,
      SysUserTaskCensusListModal
    },
    data() {
      return {
        description: '出车统计',
        // 计划年份
        year: '',
        // 查询条件
        queryParam: {taskTime:''},
        selectedRowKeys: [],
        selectedRows: [],
        loading: false,
        dataSource: [],
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '15'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        // 表头
        columns: [
          {
            title:'车辆',
            align:"center",
            dataIndex: 'license'
          },{
            title:'1月',
            align:"center",
            dataIndex: 'm1',
            scopedSlots: { customRender: 'm1' }
          },{
            title:'2月',
            align:"center",
            dataIndex: 'm2',
            scopedSlots: { customRender: 'm2' }
          },{
            title:'3月',
            align:"center",
            dataIndex: 'm3',
            scopedSlots: { customRender: 'm3' }
          },{
            title:'4月',
            align:"center",
            dataIndex: 'm4',
            scopedSlots: { customRender: 'm4' }
          },{
            title:'5月',
            align:"center",
            dataIndex: 'm5',
            scopedSlots: { customRender: 'm5' }
          },{
            title:'6月',
            align:"center",
            dataIndex: 'm6',
            scopedSlots: { customRender: 'm6' }
          },{
            title:'7月',
            align:"center",
            dataIndex: 'm7',
            scopedSlots: { customRender: 'm7' }
          },{
            title:'8月',
            align:"center",
            dataIndex: 'm8',
            scopedSlots: { customRender: 'm8' }
          },{
            title:'9月',
            align:"center",
            dataIndex: 'm9',
            scopedSlots: { customRender: 'm9' }
          },{
            title:'10月',
            align:"center",
            dataIndex: 'm10',
            scopedSlots: { customRender: 'm10' }
          },{
            title:'11月',
            align:"center",
            dataIndex: 'm11',
            scopedSlots: { customRender: 'm11' }
          },{
            title:'12月',
            align:"center",
            dataIndex: 'm12',
            scopedSlots: { customRender: 'm12' }
          },{
            title:'合计',
            align:"center",
            dataIndex: 'nums'
          }
        ],
        url: {
          list: '/cable/statisticalReport/getDepartureStatisticsList'
        }
      }
    },
    created() {
      this.getCurrentYear();
    },
    methods: {
      // 出车统计详情
      showTaskView(record, month) {
        this.$refs.SysUserTaskCensusListModal.jiaz(record.license,month,this.queryParam.taskTime);
      },
      // 计划年份
      getCurrentYear() {
        let date = new Date();
        this.year = date.getFullYear();
        this.queryParam.taskTime=this.year;
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
    }
  }
</script>

<style scoped>

</style>