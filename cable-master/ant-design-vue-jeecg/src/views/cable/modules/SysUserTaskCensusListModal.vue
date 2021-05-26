<template>
  <a-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel">

    <!-- 自定义内容-begin -->
    <a-table
      ref="table"
      size="middle"
      bordered
      rowKey="id"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :loading="loading"
      class="j-table-force-nowrap"
      @change="handleTableChange" style="margin-top: 30px">
    </a-table>
    <!-- 自定义内容-END -->

    <!-- 自定义modal页脚-begin -->
    <template slot="footer"><a-button @click="handleCancel">关闭</a-button></template>
    <!-- 自定义modal页脚-END -->

  </a-modal>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import ACol from 'ant-design-vue/es/grid/Col'
  import {getAction} from '../../../api/manage'

  export default {
    name: 'SysUserTaskCensusListModal',
    components: {
      ACol,
      AFormItem,
    },
    data() {
      return {
        width:1000,
        visible: false,
        confirmLoading: false,
        // 数据源
        dataSource: [],
        // table 加载状态
        loading: false,
        selectedRowKeys: [],
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 5,
          pageSizeOptions: ['5','6','7','8','9','10'],
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
            title:'任务日期',
            align:"center",
            dataIndex: 'taskTime'
          },{
            title:'车牌号',
            align:"center",
            dataIndex: 'license'
          },{
            title:'任务类型',
            align:"center",
            dataIndex: 'rwType_dictText'
          },{
            title:'项目编号',
            align:"center",
            dataIndex: 'projectNo'
          },{
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName'
          },{
            title:'项目地址',
            align:"center",
            dataIndex: 'projectAddress'
          }
        ],
        license:'',
        month: '',
        year:'',
      }
    },
    methods: {
      jiaz(license, month, year){
        this.license = license;
        this.month = month;
        this.year = year;
        this.loadData();
        // 显示弹出框 modal
        this.visible = true
      },
      /**
       * license 车牌号
       * month 月份
       * year 年份
       */
      loadData() {
        getAction('/cable/statisticalReport/getDepartureStatisticsDetailsList', {
          license: this.license,
          month: this.month,
          taskTime: this.year,
          pageNo:this.ipagination.current,
          pageSize:this.ipagination.pageSize,
        }).then((resp) => {
          if (resp.success) {
            this.dataSource = resp.result.records
            this.ipagination.total = resp.result.total;
          }
        })
      },
      handleCancel () {
        // 隐藏弹出框 modal
        this.visible = false;
        this.ipagination.current = 1;
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