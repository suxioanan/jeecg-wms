<template>
  <a-modal
    :title="title"
    :width="1040"
    :visible="visible"
    :confirmLoading="confirmLoading"
    cancelText="关闭"
    @ok="handleCancel"
    @cancel="handleCancel"
    >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <div style="width: 1000px;">
          <div style="margin-top: 10px;">
            <div style="margin-top: -10px;">
<!--            <div style="border: 0.5px solid #cacaca;margin-top: -10px;">-->
              <a-table
                ref="table"
                bordered
                rowKey="project_no"
                size="middle"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                @change="handleTableChange">
              </a-table>
            </div>
          </div>
        </div>
      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>
  import { getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: 'WarehouseModal',
    mixins: [JeecgListMixin],
    components: {
      JDate
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title: '',
        width: 800,
        visible: false,
        model: {},
        columns: [
          {
            title: '数量',
            align: 'center',
            dataIndex: 'inventoryQuantity'
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName'
          }
        ],
        confirmLoading: false,
        url: {
          list: '/cable/testdata/queryInventory'
        },
        dataSource: [],
      }
    },
    methods: {
      outshwo (record) {
        console.log('查看库位所传项目编号为>>>>>>', record)
        if (record.projectNo == null || record.serial == null) {
          this.$message.warning('此条信息项目编号或物料编号不存在')
          return
        }
        this.visible = true
        getAction(this.url.list, { 'projectNo': record.projectNo ,'materialId': record.serial }).then((res) => {
          if (res.success) {
            this.dataSource = res.result
          } else {
            this.$message.warning(res.message)
          }
        })
      },
      handleCancel () {
        this.$emit('close')
        this.dataSource = []
        this.visible = false
      }
    }
  }
</script>