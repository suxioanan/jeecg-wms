<!--<template>
  <a-modal
    :title="title"
    :width="1040"
    :visible="visible"
    :confirmLoading="confirmLoading"
    cancelText="关闭"
    @cancel="handleCancel"
    :footer="null">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <div style="width: 1000px;">
          <div style="margin-top: 10px;">
            <div style="border: 0.5px solid #cacaca;margin-top: -10px;">
              <a-table
                ref="table"
                bordered
                rowKey="storageLocationId"
                size="middle"
                :columns="columns"
                :dataSource="dataSource">
&lt;!&ndash;                <span slot="action" slot-scope="text, record">&ndash;&gt;



&lt;!&ndash;          </span>&ndash;&gt;

              </a-table>
            </div>
          </div>
          &lt;!&ndash;          <div style="overflow-y: scroll;overflow-x: hidden;height: 200px;">&ndash;&gt;
          &lt;!&ndash;            <div class="message" v-for="(message,index) in messageList">&ndash;&gt;
          &lt;!&ndash;              <img :src="message.picUrl" class="message-img"/>&ndash;&gt;
          &lt;!&ndash;              <span class="message-span2">{{message.wxNc}}</span>&ndash;&gt;
          &lt;!&ndash;              <p class="message-p">{{message.content}}</p>&ndash;&gt;
          &lt;!&ndash;            </div>&ndash;&gt;
          &lt;!&ndash;          </div>&ndash;&gt;
          &lt;!&ndash; 子表单区域 &ndash;&gt;
          &lt;!&ndash;            <a-tab-pane tab="客户信息" key="1" :forceRender="true">&ndash;&gt;


        </div>


      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>
  import { getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'


  export default {
    name: 'InventoryModal',
    mixins: [JeecgListMixin],
    components: {
      JDate
    },
    data() {
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
            dataIndex: 'currentInventory'
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName'
          }
        ],
        confirmLoading: false,
        url: {
          list: '/cable/testdata/keweiQuery'

        },
        dataSource: [],
      }
    },
    created() {
    },
    methods: {
      loadData(arg) {

      },
      outshwoz(record) {
        console.log('打开页面')
        console.log(record)
        console.log(record.id)
        console.log(record.id)
        console.log(record.id)
        this.visible = true
        let that = this
       var ls = record.id.split('_')
        getAction(that.url.list, { id: ls[0], type: ls[1] ,warehouseId:record.warehouseId}, 'get').then((res) => {
          if (res.success) {
            console.log('res', res)
            that.dataSource = res.result
          } else {
            that.$message.warning(res.message)
          }
        })
      },

      close() {
        this.$emit('close')
        this.dataSource = []
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }
        })
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>-->

<template>
  <a-modal
    :title="title"
    :width="1500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    cancelText="关闭"
    @cancel="handleCancel" style="margin-top: 45px">
    <a-spin :spinning="confirmLoading">

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery" style="margin-top: 0px;margin-left: 10px">
          <a-row :gutter="20">
            <a-col :md="4" :sm="24">
              <a-form-item label="项目编号">
                <a-input placeholder="请输入项目编号" v-model="queryParam.projectNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="24">
              <a-form-item label="物料描述">
                <a-input placeholder="请输入物料描述" v-model="queryParam.materialName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="24">
              <a-form-item label="仓库名称">
                <a-input placeholder="请输入仓库名称" v-model="queryParam.warehouseName"></a-input>
              </a-form-item>
            </a-col>
<!--            toggleSearchStatus-->
            <template v-if="columns1flag">
              <a-col :md="4" :sm="24">
                <a-form-item label="资产编号">
                  <a-input placeholder="请输入资产编号" v-model="queryParam.pAssetNo"></a-input>
                </a-form-item>
              </a-col>
            </template>
              <template v-if="columns3flag">
              <a-col :md="4" :sm="24">
                <a-form-item label="采购订单号">
                  <a-input placeholder="请输入采单号" v-model="queryParam.proTheorderNo"></a-input>
                </a-form-item>
              </a-col>
              <!--<a-col :md="4" :sm="24">
                <a-form-item label="库位">
                  <a-input placeholder="请输入库位" v-model="queryParam.storageLocationName"></a-input>
                </a-form-item>
              </a-col>-->
            </template>

            <a-col :md="4" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
            </span>
            </a-col>

          </a-row>
        </a-form>
      </div>


<!--      rowKey="storageLocationId"-->
      <div>
        <a-table
          ref="table"
          bordered
          size="middle"
          :loading="loading"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          @change="handleTableChange">

        <span slot="factoryText40" slot-scope="text">
          <j-ellipsis :value="text" :length="40"/>
        </span>
        </a-table>

      </div>
    </a-spin>

    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 20px;">关闭</a-button>
    </template>
  </a-modal>
</template>
<script>
  import {getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JEllipsis from '@/components/jeecg/JEllipsis'


  export default {
    name: 'InventoryModal',
    mixins: [JeecgListMixin],
    components: {
      JDate,
      JEllipsis,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '库存详情',
        width: 800,
        labelCol: {
            xs: { span: 24 },
            sm: { span: 5 },
        },
        wrapperCol: {
            xs: { span: 24 },
            sm: { span: 16 },
        },
        visible: false,
        model: {},
        // 查询条件
        queryParam: {},
        /* 查询折叠 */
        toggleSearchStatus: false,
        //资产编号
        columns1flag :false,
        //采购订单号
        columns3flag :false,
        id: '',
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
        columns:[],
        columns1: [
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo'
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText40' }
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'materialName',
            scopedSlots: { customRender: 'factoryText40' }
          },
          {
            title: '资产编号',
            align: 'center',
            dataIndex: 'passetNo'
          },
          {
            title: '库存数',
            align: 'center',
            dataIndex: 'inventoryQuantity',
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'dw_dictText',
          },
          {
            title: '仓库名称',
            align: 'center',
            dataIndex: 'warehouseName'
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName'
          },
        ],
        columns3: [
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo'
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText40' }
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'materialName',
            scopedSlots: { customRender: 'factoryText40' }
          },
          {
              title: '采购订单号',
              align: 'center',
              dataIndex: 'proTheorderNo'
          },
          {
            title: '库存数',
            align: 'center',
            dataIndex: 'inventoryQuantity',
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'dw_dictText',
          },
          {
            title: '仓库名称',
            align: 'center',
            dataIndex: 'warehouseName'
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName'
          },
        ],
        columns4: [
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo'
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText40' }
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'materialName',
            scopedSlots: { customRender: 'factoryText40' }
          },
          {
            title: '库存数',
            align: 'center',
            dataIndex: 'inventoryQuantity',
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'dw_dictText',
          },
          {
            title: '重量',
            align: 'center',
            dataIndex: 'availableWeight',
            customRender: (text,record) => {
              if (record.availableWeight != '' && record.availableWeight != null)
                return record.availableWeight + '吨'
            }
          },
          {
            title: '仓库名称',
            align: 'center',
            dataIndex: 'warehouseName'
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName'
          },
        ],
        confirmLoading: false,
        url: {
          list: '/cable/warehouse/selectInfo'
        },
        dataSource: [],
        materialName:'',
      }
    },
    created() {
    },
    methods: {
      loadData() {
        this.loading = true
        let params = Object.assign(this.queryParam)
        params.serial = this.id
        params.recyclingSpecifications = this.materialName
        params.pageNo = this.ipagination.current,
        params.pageSize = this.ipagination.pageSize
        console.log(params)
        //TODO 物料编号，页面初始化查询条件
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            let type = this.dataSource[0].ptype
            console.log("this.type",type)
            if (type == '3') {
              this.columns = this.columns3
              this.columns3flag = true
            } else if (type == '4' || type == null) {
              this.columns = this.columns4
            }else if(type == '1' || type == '2'){
              this.columns = this.columns1
              this.columns1flag = true
            }
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      outshwoz(record) {
        console.log("详情页面》》》：",record)
        this.visible = true
        //TODO 物料编号，页面初始化查询条件
        this.id = record.serial
        this.materialName = record.materialName
        this.loadData();
      },
      handleCancel() {
        this.$emit('close')
        this.dataSource = []
        //TODO 清空查询条件
        this.queryParam = {}
        this.record = {}
        this.columns = []
        this.visible = false
        this.toggleSearchStatus = false
        //资产编号
        this.columns1flag = false
        //采购订单号
        this.columns3flag = false
      },
      handleTableChange(pagination, filters, sorter) {
          //TODO 筛选
          if (Object.keys(sorter).length > 0) {
              this.isorter.column = sorter.field;
              this.isorter.order = 'ascend' === sorter.order ? 'asc' : 'desc';
          }
          this.ipagination = pagination;
          this.loadData();
      },

      // close() {
      //   this.$emit('close')
      //   this.dataSource = []
      //   this.visible = false
      // },
      // handleOk() {
      //   const that = this
      //   // 触发表单验证
      //   this.form.validateFields((err, values) => {
      //     if (!err) {
      //       that.confirmLoading = true
      //       let httpurl = ''
      //       let method = ''
      //       if (!this.model.id) {
      //         httpurl += this.url.add
      //         method = 'post'
      //       } else {
      //         httpurl += this.url.edit
      //         method = 'put'
      //       }
      //       let formData = Object.assign(this.model, values)
      //       console.log('表单提交数据', formData)
      //       httpAction(httpurl, formData, method).then((res) => {
      //         if (res.success) {
      //           that.$message.success(res.message)
      //           that.$emit('ok')
      //         } else {
      //           that.$message.warning(res.message)
      //         }
      //       }).finally(() => {
      //         that.confirmLoading = false
      //         that.close()
      //       })
      //     }
      //   })
      // },

    }
  }
</script>