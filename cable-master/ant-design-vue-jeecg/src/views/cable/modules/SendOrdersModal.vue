<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @cancel="handleCancel"
    cancelText="关闭"style="margin-top:100px">
    <a-table
      ref="table"
      bordered
      size="middle"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      @change="handleTableChange"
      style="margin-top:15px">

      <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>

    </a-table>
  </j-modal>
</template>

<script>

  import {filterObj} from '@/utils/util';
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {httpAction, getAction, putAction, deleteAction} from '@/api/manage'
  import JEllipsis from '@/components/jeecg/JEllipsis'


  export default {
    name: "VehicleTaskModal",
    components: {
      JEllipsis
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"",
        width:1000,
        visible: false,
        dataSource: [],
        ipagination: {
          pageNo: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '10'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        columns: [
          {
            title:'车牌号码',
            align:"center",
            dataIndex: 'license',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'计划类型',
            align:"center",
            dataIndex: 'planType',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'操作模式',
            align:"center",
            dataIndex: 'operatorSchema_dictText',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'仓库名称',
            align:"center",
            dataIndex: 'warehouseName',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'项目编码',
            align:"center",
            dataIndex: 'projectNo',
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'项目地址',
            align:"center",
            dataIndex: 'engineeringAddress',
            scopedSlots: { customRender: 'factoryText' },
          }
        ],
        queryParam: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        validatorRules: {
        },
        url: {
          taskList: "/cable/sendOrders/taskList",
        },
        date:'',
        val:'',
      }
    },
    created () {

    },
    methods: {
      data(value){
        console.log('data'+value)
        getAction(this.url.taskList,{date: value}).then((res) => {
          if (res.success) {
            console.log('数据接收')
            console.log(res)
            this.dataSource = res.result.records;
            console.log(this.dataSource)
          }
        })
        this.visible = true;
        console.log('页面打开'+value)
      },
      agentSettings(value){
        this.date = value;
        console.log('agentSettings'+value)
        this.data(value);
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        console.log('分页');
        console.log(pagination);
        console.log(filters);
        console.log(sorter);
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        /*this.ipagination = pagination;*/
        this.data(this.date);
      },
    }
  }
</script>