<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    @ok="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">

      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery" style="margin-top: 20px">
          <a-row :gutter="12">
            <a-col :md="6" :sm="24">
              <a-form-item label="工程名称">
                <a-input v-model="queryParam.projectName" placeholder="请输入工程名称" ></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="24">
              <a-form-item label="资产编码">
                <a-input v-model="queryParam.assetNo" placeholder="请输入资产编码" ></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="24">
              <a-form-item label="采购订单号">
                <a-input v-model="queryParam.proTheorderNo" placeholder="请输入采购订单号" ></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <a-table
        ref="table"
        bordered
        rowKey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        @change="handleTableChange" style="margin-top: 15px">
        <span slot="factoryText10" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>
        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="20"/>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="yiDon(record)">移动物料</a>
        </span>
      </a-table>
      <material-storage-location-modal ref="materialStorageLocationModal"
                                       @ok="modalFormOk"></material-storage-location-modal>
    </a-spin>
  </j-modal>
</template>

<script>
    import MaterialStorageLocationModal from './MaterialStorageLocationModal'
    import {getAction} from '@/api/manage'
    import JEllipsis from '@/components/jeecg/JEllipsis'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import JDate from '@/components/jeecg/JDate'

    export default {
        name: 'StorageLocationModal',
        mixins:[JeecgListMixin],
        components: {
            JDate,
            MaterialStorageLocationModal,
            JEllipsis
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                width: 1100,
                loading: false,
                visible: false,
                model: {},
                dataSource: [],
                ipagination: {
                    pageNo: 1,
                    pageSize: 5,
                    pageSizeOptions: ['5', '10'],
                    showTotal: (total, range) => {
                        return range[0] + '-' + range[1] + ' 共' + total + '条'
                    },
                    showQuickJumper: true,
                    showSizeChanger: true,
                    total: 0
                },
                //查询条件
                queryParam: {storageLocationId:''},
                columns: [
                    {
                        title: '工程账号',
                        align: 'center',
                        dataIndex: 'projectNo',
                    },
                    {
                        title: '工程名称',
                        align: 'center',
                        dataIndex: 'projectName',
                        scopedSlots: {customRender: 'factoryText10'}
                    },
                    {
                        title: '物料编号',
                        align: 'center',
                        dataIndex: 'serial',
                    },
                    {
                        title: '物料名称',
                        align: 'center',
                        dataIndex: 'materialName',
                        scopedSlots: {customRender: 'factoryText10'}
                    },
                    {
                        title: '资产编号',
                        align: 'center',
                        dataIndex: 'assetNo',
                    },
                    {
                        title: '采购订单号',
                        align: 'center',
                        dataIndex: 'proTheorderNo',
                    },
                    {
                        title: '库存数',
                        align: 'center',
                        dataIndex: 'quantityInStock',
                    },
                    {
                        title: '单位',
                        align: 'center',
                        dataIndex: 'unit_dictText',
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        // fixed:"right",
                        width: 147,
                        scopedSlots: {customRender: 'action'}
                    }
                ],
                url: {
                    list: '/cable/inventory/insurancePageList',
                },
                confirmLoading: false,
                validatorRules: {},
                storageLocationId: ''
            }
        },
        created() {
        },
        methods: {
            modalFormOk() {
                this.loadData()
            },
            yiDon(val) {
                console.log('移动物料数据', val)
                console.log('移动物料库位 id 为', this.storageLocationId)
                this.$refs.materialStorageLocationModal.yiDon(val, this.storageLocationId)
                this.$refs.materialStorageLocationModal.title = ''
            },
            loadData() {
                console.log("进入了方法2222222222222222》》》》》》》》")
                this.queryParam.storageLocationId = this.storageLocationId
                this.queryParam.pageNo = this.ipagination.current,
                this.queryParam.pageSize = this.ipagination.pageSize
                getAction('/cable/inventory/insurancePageList', this.queryParam).then((res) => {
                    if (res.success) {
                        this.dataSource = res.result.records
                        this.ipagination.total = res.result.total;
                        console.log('库位的库存信息', res)
                    }
                })
            },
            showStorageLocation(wa, val) {
                console.log("进入了方法1！！！111111》》》》》》》》")
                this.storageLocationId = val
                this.loadData()
                this.visible = true
            },
            handleTableChange(pagination, filters, sorter) {
                //分页、排序、筛选变化时触发
                //TODO 筛选
                if (Object.keys(sorter).length > 0) {
                    this.isorter.column = sorter.field
                    this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
                }
                /*this.ipagination = pagination;*/
                this.data(this.date)
            },
            close() {
                // this.$emit('close')
                this.$emit('ok')
                this.visible = false
            },
            handleCancel() {
                console.log('确定')
                this.close()
            }

        }
    }

</script>
<style>

  .yida {
    display: inline-block;
    margin-top: 20px;
    margin-left: 15px;
    width: 80px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    border-radius: 5px;
    background-color: #afafaf;
    color: white;
  }
</style>