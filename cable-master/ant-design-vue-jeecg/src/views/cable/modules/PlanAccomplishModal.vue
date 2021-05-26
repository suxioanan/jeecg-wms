<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top:100px">
    <a-table
      ref="table"
      rowKey="id"
      bordered
      size="middle"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      @change="handleTableChange" style="margin-top: 15px">

      <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
      </span>

      <span slot="anomalousCause" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>
      <span slot="operatorSchema" slot-scope="text,record">
          <template v-if="record.operatorSchema===0">
            出库
          </template>
           <template v-else-if="record.operatorSchema===1">
           入库
          </template>
       <template v-else>
         未知 {{record.operatorSchema}}
       </template>
      </span>
      <span slot="state" slot-scope="text,record">
          <template v-if="record.state===0">
            未完单
          </template>
           <template v-else-if="record.state===1">
           已完单
          </template>
      </span>
      <span slot="sceneSituation" slot-scope="text,record">
          <template v-if="record.sceneSituation===0">
            正常
          </template>
           <template v-else-if="record.sceneSituation===1">
           异常
          </template>
       <template v-else>
<!--         未知 {{record.operatorSchema}}-->
       </template>
      </span>
      <span slot="receiptPhotos" slot-scope="text,record">
        <template v-if="record.receiptPhotos!=undefined&&record.receiptPhotos!=null&&record.receiptPhotos!=''">
           <a @click="() => getUrlNewView(record.receiptPhotos)">点我查看</a>
        </template>
       <template v-else>
<!--         暂无-->
       </template>
      </span>
      <span slot="scenePhotos1" slot-scope="text,record">
        <template v-if="record.scenePhotos1!=undefined&&record.scenePhotos1!=null&&record.scenePhotos1!=''">
           <a @click="() => getUrlNewView(record.scenePhotos1)">点我查看</a>
        </template>
       <template v-else>
<!--         暂无-->
       </template>
      </span>
      <span slot="scenePhotos2" slot-scope="text,record">
        <template v-if="record.scenePhotos2!=undefined&&record.scenePhotos2!=null&&record.scenePhotos2!=''">
           <a @click="() => getUrlNewView(record.scenePhotos2)">点我查看</a>
        </template>
       <template v-else>
<!--         暂无-->
       </template>
      </span>
      <span slot="action" slot-scope="text, record">
        <template v-if="record.state==0">
           <a @click="ss(record)">完单操作</a>
        </template>

      </span>

    </a-table>
    <plan-accomplish-send-orders-modal ref="planAccomplishSendOrdersModal"
                                       @ok="modaeFormOk"></plan-accomplish-send-orders-modal>

    <!-- 自定义页脚 -->
    <template slot="footer">
      <a-button key="back" @click="handleCancel">
        关闭
      </a-button>
    </template>
  </a-modal>
</template>

<script>
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { filterObj } from '@/utils/util'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { httpAction, getAction, putAction, deleteAction } from '@/api/manage'
  import PlanAccomplishSendOrdersModal from './PlanAccomplishSendOrdersModel'

  export default {
    name: 'PlanAccomplishModal',
    components: {
      PlanAccomplishSendOrdersModal,
      JEllipsis
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '',
        width: 1200,
        visible: false,
        dataSource: [],
        ipagination: {
          pageNo: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '6', '7', '8', '9', '10'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        columns: [
          {
            title: '派单类型',
            align: 'center',
            dataIndex: 'operatorSchema',
            scopedSlots: { customRender: 'operatorSchema' }
            // dataIndex: 'operatorSchema_dictText'
          },
          {
            title: '目标仓库',
            align: 'center',
            dataIndex: 'warehouseName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '派单时间',
            align: 'center',
            dataIndex: 'sendOrdersTime'
          },
          {
            title: '任务日期',
            align: 'center',
            dataIndex: 'accomplishTime'
          },
          {
            title: '完单状态',
            align: 'center',
            dataIndex: 'state',
            scopedSlots: { customRender: 'state' }
            // customRender(text) {
            //   console.log(text)
            //   if (text === 0) {
            //     return '未完单'
            //   } else if (text === 1) {
            //     return '已完单'
            //   }
            // }
            //dataIndex: 'state_dictText'
          },
          {
            title: '完单数量',
            align: 'center',
            dataIndex: 'accomplishNum'
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'accomplishNumUnit',
            customRender: (val, row, index) => {
              for (let item of this.unitLists) {
                if (val == item.itemValue) {
                  return item.itemText
                }
              }
            }
          },
          {
            title: '回单图片',
            align: 'center',
            dataIndex: 'receiptPhotos',
            scopedSlots: { customRender: 'receiptPhotos' }
          },
          {
            title: '是否异常',
            align: 'center',
            dataIndex: 'sceneSituation',
            scopedSlots: { customRender: 'sceneSituation' }
            //dataIndex: 'sceneSituation_dictText'
          },
          {
            title: '异常原因',
            align: 'center',
            dataIndex: 'anomalousCause',
            scopedSlots: { customRender: 'anomalousCause' }

          },
          {
            title: '异常照片1',
            align: 'center',
            dataIndex: 'scenePhotos1',
            scopedSlots: { customRender: 'scenePhotos1' }
          },
          {
            title: '异常照片2',
            align: 'center',
            dataIndex: 'scenePhotos2',
            scopedSlots: { customRender: 'scenePhotos2' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            // fixed:"right",
            width: 147,
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
        date: '',
        val: '',
        planTy: '',
        sendOrdersId: '',
        unitLists: {}
      }
    },
    created() {
      this.unlisList()
    },
    methods: {
      unlisList() {
        getAction('/sys/dictItem/getDictItemUnit').then((res) => {
          if (res.success) {
            this.unitLists = res.result
          }
        })
      },
      modaeFormOk() {
        this.data(this.date.projectNo, this.date.id, this.planTy, null)
      },
      getUrlNewView(textUrl) {
        let config = window._CONFIG['domianURL'] + '/sys/common/view'
        window.open(config + '/' + textUrl, '_blank')
      },
      ss(val) {
        //资产编号带到提交完单页面
        val.assetNo = this.data.assetNo
        if (this.val != null) {
          if (this.val.storageLocationId != null) {
            this.$refs.planAccomplishSendOrdersModal.dkwdcz(val, this.planTy, this.val.storageLocationId)
          } else {
            this.$refs.planAccomplishSendOrdersModal.dkwdcz(val, this.planTy, null)
          }
        } else {
          this.$refs.planAccomplishSendOrdersModal.dkwdcz(val, this.planTy, null)
        }
        this.$refs.planAccomplishSendOrdersModal.title = ''
      },
      data(projectNo, id, planTy, sendOrdersId) {
        if (sendOrdersId == null) {
          sendOrdersId = 'www'
        }
        var obj = {
          projectNo: projectNo,
          planId: id,
          planType: planTy,
          sendOrdersId: sendOrdersId
        }
        getAction('/cable/sendOrders/selectPlanAccomplish', obj).then((res) => {
          if (res.success) {
            this.dataSource = res.result
          }
        })
        this.visible = true
      },
      dakwd(value, planTy, sendOrdersId, val) {
        if (val != null) {
          if (val.storageLocationId != null) {
            this.val = val
          }
        }
        this.date = value
        this.planTy = planTy
        this.sendOrdersId = sendOrdersId
        this.data(value.projectNo, value.id, planTy, sendOrdersId)
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleCancel() {
        this.close()
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        /*this.ipagination = pagination;*/
        this.data(this.date.projectNo, this.date.id, this.planTy)
      }
    }
  }
</script>