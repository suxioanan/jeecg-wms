<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @cancel="handleCancela"
    cancelText="关闭" style="margin-top:50px">
    <a-table
      ref="table"
      bordered
      rowKey="id"
      size="middle"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      @change="handleTableChange" style="margin-top: 20px">

      <span slot="factoryText10" slot-scope="text">
        <j-ellipsis :value="text" :length="10"/>
      </span>
      <span slot="factoryText" slot-scope="text">
        <j-ellipsis :value="text" :length="7"/>
      </span>

      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">修改信息</a>
      </span>
    </a-table>

    <!-- 自定义页脚-begin -->
    <template slot="footer">
      <a-button @click="handleCancela">关闭</a-button>
    </template>
    <!-- 自定义页脚-END -->

    <plan-the-same-day-de-modal ref="planTheSameDayDeModal" @ok="modalFormOk"></plan-the-same-day-de-modal>
  </j-modal>
</template>
<script>
  import { httpAction, getAction, putAction, deleteAction } from '@/api/manage'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import PlanTheSameDayDeModal from './PlanTheSameDayDeModal'

  export default {
    name: 'PlanSendOrdersTheSameDayModal',
    components: {
      JEllipsis,
      PlanTheSameDayDeModal
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '',
        width: 1400,
        visible: false,
        dataSource: [],
        ipagination: {
          current: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '6', '7', '8', '9', '10'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        sendOrdersTypes: [],
        columns: [
          {
            title: '派单类型',
            align: 'center',
            dataIndex: 'operatorSchema',
            width: 80,
            customRender: (value, row, index) => {
              var s = ''
              for (let item of this.sendOrdersTypes) {
                if (value == item.itemValue) {
                  s = item.itemText
                }
              }
              return s
            }
          },
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'pplanType',
            width: 80
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            width:200,
            scopedSlots: { customRender: 'factoryText10' }
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'rawMaterialText',
            width: 180,
            scopedSlots: { customRender: 'factoryText10' }
          },
          {
            title: '派单数量',
            align: 'center',
            dataIndex: 'backup1',
            width: 80,
          },
          {
            title: '任务地址',
            align: 'center',
            dataIndex: 'address',
            width:200,
            scopedSlots: { customRender: 'factoryText10' }
          },
          {
            title: '联系人',
            align: 'center',
            dataIndex: 'linkman',
            width: 150
          },
          {
            title: '联系电话',
            align: 'center',
            dataIndex: 'phone',
            width: 100
          },
          {
            title: '派单车辆',
            align: 'center',
            dataIndex: 'a0',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '派单人员',
            align: 'center',
            dataIndex: 'a1',
            scopedSlots: { customRender: 'factoryText' },
            /*customRender: (value, row, index) => {
              if (value == null) {
                return '未安排人员'
              }
              value = value.split(',')
              var s = ''
              for (let item of this.users) {
                for (let val of value) {
                  if (val == item.id) {
                    s += item.realname + ','
                  }
                }
              }

              if (s.length > 0) {
                s = s.substring(0, s.length - 1)
                return s
              } else {
                return '未安排人员'
              }

            },*/
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        queryParam: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        validatorRules: {},
        url: {
          lists: '/cable/sendOrders/selectPlanSendOrdersTheSameDay'
        },
        users: []
      }
    },
    created() {
      this.usersList()
      this.sendOrdersType()
    },
    methods: {
      //TODO 派单类型
      sendOrdersType() {
        this.sendOrdersTypes = ''
        getAction('/sys/dictItem/getDictItemSendOrdersType').then((res) => {
          if (res.success) {
            console.log(res)
            console.log('派单类型')
            this.sendOrdersTypes = res.result
          }
        })
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.data();
      },
      usersList() {
        this.users = ''
        getAction('/cable/plan2/queryUserList').then((res) => {
          if (res.success) {
            this.users = res.result
          }
        })
      },
      handleEdit(record) {
        this.$refs.planTheSameDayDeModal.theSames(record);
        this.$refs.planTheSameDayDeModal.title = '';
      },
      theSameDays() {
        // 今日派单页面
        this.data()
        this.visible = true
      },
      data() {
        let param = {
          pageNo : this.ipagination.current,
          pageSize : this.ipagination.pageSize
        }
        getAction(this.url.lists, param).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
        })
        this.visible = true
      },
      handleCancela() {
        this.visible = false
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        this.ipagination=pagination;
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        /*this.ipagination = pagination;*/
        this.data()
      }
    }
  }
</script>