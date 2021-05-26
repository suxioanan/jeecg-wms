<template>
  <a-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel" style="margin-top: 50px">

    <!-- 自定义内容-begin -->
    <a-table
      ref="table"
      size="middle"
      bordered
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :loading="loading"
      class="j-table-force-nowrap"
      @change="handleTableChange" style="margin-top: 30px">
      <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
      </span>
      <!-- 物料数量 -->
      <span slot="numReceiptsText" slot-scope="text, record">
        {{record.numReceipts+record.wasteMaterialUnit_dictText}}
      </span>
    </a-table>
    <!-- 自定义内容-END -->

    <!-- 自定义modal页脚-begin -->
    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
    </template>
    <!-- 自定义modal页脚-END -->

  </a-modal>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import { getAction } from '../../../api/manage'
  import JEllipsis from '../../../components/jeecg/JEllipsis'

  export default {
    name: 'SettleAccountsDetailsModal',
    components: {
      JEllipsis
    },
    data() {
      return {
        users: {},
        width: 1500,
        visible: false,
        confirmLoading: false,
        // 数据源
        dataSource: [],
        // table 加载状态
        loading: false,
        selectedRowKeys: [],
        // 分页参数
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
        // 表头
        columns: [
          // {
          //   title: '任务车辆',
          //   align: 'center',
          //   dataIndex: 'a0',
          //   scopedSlots: { customRender: 'factoryText' }
          // },
          {
            title: '物料名称',
            align: 'center',
            width: 200,
            dataIndex: 'wasteMaterialText'
            // rawMaterialText
          },
          {
            title: '入库数量',
            align: 'center',
            width: 100,
            dataIndex: 'receivingNum'
          },
          {
            title: '出库数量',
            align: 'center',
            width: 100,
            dataIndex: 'deliverNum'
          },
          {
            title: '采购订单号',
            align: 'center',
            width: 200,
            dataIndex: 'proTheorderNo'
          },
          /*{
            title: '计划类型',
            align: 'center',
            dataIndex: 'pplanType'
          }, {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            title: '任务人员',
            align: 'center',
            dataIndex: 'a1',
            scopedSlots: { customRender: 'factoryText' },
            customRender: (value, row, index) => {
              value=value.split(",");
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

            },
          }, {
            title: '联系人',
            align: 'center',
            dataIndex: 'linkman'
          }, {
            title: '联系电话',
            align: 'center',
            dataIndex: 'phone'
          }, {
            title: '地址',
            align: 'center',
            dataIndex: 'address',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            title: '任务时间',
            align: 'center',
            dataIndex: 'taskTime'
          }*/
        ]
      }
    },
    created(){

    },
    methods: {
      usersList() {
        this.users = []
        getAction('/cable/plan2/queryUserList').then((res) => {
          if (res.success) {
            this.users = res.result
            console.log(this.users)
            console.log(this.users)
            console.log(this.users)
          }
        })
      },
      // 展示计划结算详情页面
      showDetail(record) {
        this.usersList()
        this.visible = true
        getAction('/cable/plan1/selectSettleAccountsDetails', {
          projectNo: record.projectNo
        }).then((resp) => {
          if (resp.success) {
            this.dataSource = resp.result.records
          }
        })
      },
      // 隐藏弹出框 modal
      handleCancel() {
        this.visible = false
      },
      //分页、排序、筛选变化时触发
      handleTableChange(pagination, filters, sorter) {
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination = pagination
        this.loadData()
      }
    }
  }
</script>

<style scoped>

</style>