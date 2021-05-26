<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="物料名称">
              <a-input v-model="queryParam.materialName" placeholder="请输入物料名称"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料编号">
              <a-input v-model="queryParam.materialSerial" placeholder="请输入物料编号"></a-input>
            </a-form-item>
          </a-col>

<!--          <a-col :md="3" :sm="7">-->
<!--            <a-form-item label="周">-->
<!--              <a-select v-model="queryParam.week" placeholder="请选择周" @change="weeks">-->
<!--                <a-select-option value="1">本周</a-select-option>-->
<!--                <a-select-option value="2">上周</a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="3" :sm="7">-->
<!--            <a-form-item label="月">-->
<!--              <a-select v-model="queryParam.month" placeholder="请选择月份" @change="months">-->
<!--                <a-select-option value="1">1月</a-select-option>-->
<!--                <a-select-option value="2">2月</a-select-option>-->
<!--                <a-select-option value="3">3月</a-select-option>-->
<!--                <a-select-option value="4">4月</a-select-option>-->
<!--                <a-select-option value="5">5月</a-select-option>-->
<!--                <a-select-option value="6">6月</a-select-option>-->
<!--                <a-select-option value="7">7月</a-select-option>-->
<!--                <a-select-option value="8">8月</a-select-option>-->
<!--                <a-select-option value="9">9月</a-select-option>-->
<!--                <a-select-option value="10">10月</a-select-option>-->
<!--                <a-select-option value="11">11月</a-select-option>-->
<!--                <a-select-option value="12">12月</a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="3" :sm="7">-->
<!--            <a-form-item label="季">-->
<!--              <a-select v-model="queryParam.quarter" placeholder="请选择季度" @change="quarters">-->
<!--                <a-select-option value="1">春</a-select-option>-->
<!--                <a-select-option value="2">夏</a-select-option>-->
<!--                <a-select-option value="3">秋</a-select-option>-->
<!--                <a-select-option value="4">冬</a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="3" :sm="7">-->
<!--            <a-form-item label="年">-->
<!--              <a-select v-model="queryParam.year" placeholder="请选择年份" @change="yeare">-->
<!--                <a-select-option :value="year">{{year}}</a-select-option>-->
<!--                <a-select-option :value="year-1">{{year-1}}</a-select-option>-->
<!--                <a-select-option :value="year-2">{{year-2}}</a-select-option>-->
<!--                <a-select-option :value="year-3">{{year-3}}</a-select-option>-->
<!--                <a-select-option :value="year-4">{{year-4}}</a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <!--<a-col :md="4" :sm="7">
            <a-form-item label="开始时间">
              <j-date v-model="queryParam.startTime" placeholder="请选择开始日期"></j-date>

            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="7">
            <a-form-item label="结束时间">
              <j-date v-model="queryParam.endTime" placeholder="请选择结束日期"></j-date>
            </a-form-item>
          </a-col>-->

          <a-col :md="4" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
             <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{selectedRowKeys.length}}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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

        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="20"/>
        </span>

        <!-- table操作功能-begin -->
        <span slot="action" slot-scope="text, record">
          <a @click="particulars(record)">详情</a>
        </span>
        <!-- table操作功能-END -->

      </a-table>
    </div>
    <!-- table区域-END -->

    <material-out-put-account-modal ref="materialOutPutAccountModal"></material-out-put-account-modal>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import MaterialOutPutAccountModal from './modules/MaterialOutPutAccountModal'
  import {putAction, postAction, getAction} from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'MaterialOutPutAccount',
    mixins: [JeecgListMixin],
    components: {MaterialOutPutAccountModal, JEllipsis,JDate},
    data() {
      return {
        description: '物料出入库台账',
        year: '',
        queryParam: {
          week: undefined,
          month: undefined,
          quarter: undefined,
          year: undefined,
          startTime:'',
          endTime:'',
        },
        columns: [
          {
            title: '物料名称',
            align: 'center',
            width: 350,
            dataIndex: 'materialName',
            scopedSlots: {customRender: 'factoryText'}
          }, {
            title: '物料编号',
            align: 'center',
            width: 350,
            dataIndex: 'materialSerial',
            scopedSlots: {customRender: 'factoryText'}
          }, {
            title: '规格型号',
            align: 'center',
            dataIndex: 'materialAtions',
            scopedSlots: {customRender: 'factoryText'}
          },
          // {
          //   title: '供应商',
          //   align: 'center',
          //   dataIndex: 'materialSupplier',
          //   scopedSlots: { customRender: 'factoryText' }
          // },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'materialUnit_dictText',
            scopedSlots: {customRender: 'factoryText'}
          }, {
            title: '入库数量',
            align: 'center',
            dataIndex: 'deliverNum'
          }, {
            title: '出库数量',
            align: 'center',
            dataIndex: 'receivingNum'
          },
          // {
          //   title: '余留数量',
          //   align: 'center',
          //   dataIndex: 'ylNum'
          // },
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align: 'center',
          //   width: 147,
          //   scopedSlots: { customRender: 'action' }
          // }
        ],
        url: {}
      }
    },
    methods: {
      searchQuery() {
        this.loadData2(1)
      },
      searchReset() {
        this.queryParam = {}
        let date = new Date()
        let months = date.getMonth() + 1
        this.queryParam.year = date.getFullYear()
        this.queryParam.month = months + ''
        this.loadData2(1)
      },
      loadData2(arg) {
        // if (!this.url.list) {
        //   this.$message.error('请设置url.list属性!')
        //   return
        // }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        var params = this.getQueryParams()//查询条件
        params.materialName = this.queryParam.materialName
        params.materialSerial = this.queryParam.materialSerial
        params.week = this.queryParam.week
        params.month = this.queryParam.month
        params.startTime = this.queryParam.startTime==undefined?'':this.queryParam.startTime
        params.endTime = this.queryParam.endTime==undefined?'':this.queryParam.endTime
        this.loading = true
        getAction('/cable/material/getMaterialOutPutAccountList', params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      months(val) {
        this.queryParam.month = val
        this.queryParam.quarter = undefined
        this.queryParam.week = undefined
        if (this.queryParam.year == undefined || this.queryParam.year == '') {
          let date = new Date()
          this.queryParam.year = date.getFullYear()
        }
      },
      yeare(val) {
        this.queryParam.year = val
        this.queryParam.month = undefined
        this.queryParam.week = undefined
        this.queryParam.quarter = undefined
      },
      quarters(val) {
        this.queryParam.quarter = val
        this.queryParam.month = undefined
        this.queryParam.week = undefined
        if (this.queryParam.year == undefined || this.queryParam.year == '') {
          let date = new Date()
          this.queryParam.year = date.getFullYear()
        }
      },
      weeks(val) {
        this.queryParam.week = val
        this.queryParam.month = undefined
        this.queryParam.year = undefined
        this.queryParam.quarter = undefined
      },
      particulars(record) {
        this.$refs.materialOutPutAccountModal.particularsShow(record)
        this.$refs.materialOutPutAccountModal.title = ''
      },
      // 计划年份
      getCurrentYear() {
        let date = new Date()
        this.queryParam.year = date.getFullYear()
        this.queryParam.month = date.getMonth() + 1
      }
    },
    mounted() {
      this.getCurrentYear()
      this.loadData2(1)
    }
  }
</script>