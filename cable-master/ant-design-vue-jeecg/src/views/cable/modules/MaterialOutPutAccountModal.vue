<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="5" :sm="7">
              <a-form-item label="周">
                <a-select v-model="queryParam.week" placeholder="请选择周" @change="weeks">
                  <a-select-option value="1">本周</a-select-option>
                  <a-select-option value="2">上周</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="5" :sm="7">
              <a-form-item label="月">
                <a-select v-model="queryParam.month" placeholder="请选择月份" @change="months">
                  <a-select-option value="1">1月</a-select-option>
                  <a-select-option value="2">2月</a-select-option>
                  <a-select-option value="3">3月</a-select-option>
                  <a-select-option value="4">4月</a-select-option>
                  <a-select-option value="5">5月</a-select-option>
                  <a-select-option value="6">6月</a-select-option>
                  <a-select-option value="7">7月</a-select-option>
                  <a-select-option value="8">8月</a-select-option>
                  <a-select-option value="9">9月</a-select-option>
                  <a-select-option value="10">10月</a-select-option>
                  <a-select-option value="11">11月</a-select-option>
                  <a-select-option value="12">12月</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="5" :sm="7">
              <a-form-item label="季">
                <a-select v-model="queryParam.quarter" placeholder="请选择季度" @change="quarters">
                  <a-select-option value="1">春</a-select-option>
                  <a-select-option value="2">夏</a-select-option>
                  <a-select-option value="3">秋</a-select-option>
                  <a-select-option value="4">冬</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="5" :sm="7">
              <a-form-item label="年">
                <a-select v-model="queryParam.year" placeholder="请选择年份" @change="yeare">
                  <a-select-option :value="year">{{year}}</a-select-option>
                  <a-select-option :value="year-1">{{year-1}}</a-select-option>
                  <a-select-option :value="year-2">{{year-2}}</a-select-option>
                  <a-select-option :value="year-3">{{year-3}}</a-select-option>
                  <a-select-option :value="year-4">{{year-4}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="2" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
<!--             <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
            </a-col>

          </a-row>
        </a-form>
      </div>

      <div>
        <a-tabs defaultActiveKey="1">
          <a-tab-pane key="1" >
            <bar-multid  title="物料出入统计" :height="420" :dataSource="jhjgData" :fields="jhjgFields"/>
          </a-tab-pane>
        </a-tabs>

      </div>

    </a-spin>
  </a-modal>
</template>

<script>

  import Bar from '@/components/chart/Bar'
  import '@/assets/less/TableExpand.less'
  import pick from 'lodash.pick'
  import { putAction, postAction, getAction } from '@/api/manage'
  import { DataSet } from '@antv/data-set'
  import BarMultid from '@/components/chart/BarMultid'

  export default {
    name: 'MaterialOutPutAccountModal',
    components: {
      Bar, BarMultid
    },
    data() {
      return {
        jhjgData: [],
        jhjgFields: [],
        barData: [],
        tabName: '年月周',
        // barData: [],
        value: 1,
        title: '操作',
        width: 1200,
        visible: false,
        model: {},
        queryParam: {
          week: undefined,
          month: '',
          quarter: undefined,
          year: undefined
        },
        year: '',
        valuee: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {},
      }
    },
    created() {

    },
    methods: {
      yeare() {
        this.queryParam.quarter = ''
        this.queryParam.month = ''
        this.queryParam.week = ''
        this.loadData()
      },
      quarters(){
        if(this.queryParam.year==null || this.queryParam.year=='' || this.queryParam.year==undefined){
          let date = new Date()
          this.queryParam.year = date.getFullYear();
        }
        this.queryParam.month = ''
        this.queryParam.week = ''
        this.loadData()
      },
      months(){
        if(this.queryParam.year==null || this.queryParam.year=='' || this.queryParam.year==undefined){
          let date = new Date()
          this.queryParam.year = date.getFullYear();
        }
        this.queryParam.quarter = ''
        this.queryParam.week = ''
        this.loadData()
      },
      weeks() {
        this.queryParam.month = ''
        this.queryParam.year = ''
        this.queryParam.quarter = ''
        this.loadData()
      },
      searchReset() {
        this.queryParam = {}
        let date = new Date()
        this.queryParam.month = date.getMonth() + 1
        this.queryParam.year = date.getFullYear();
        this.loadData()
      },
      loadData() {
        this.barData.length = 0;
        this.loading = true
        var params = Object.assign(this.queryParam)
        params.id = this.valuee.id
        console.log(params)
        getAction('/cable/material/materialOutPutList', params).then((res) => {
          if (res.success) {
            console.log(res)
            this.barData =res.result;
            console.log(this.barData)
            this.ban();
          }
          this.loading = false
        })
      },
      ban(){
        console.log("绑定")
        this.jhjgFields.length = 0;
        this.jhjgData = [ this.barData[0] , this.barData[1] ]
        let map = this.barData[0];
        if(this.queryParam.quarter!=undefined&&this.queryParam.quarter!=''){
          console.log('季')
          this.jhjgFields.length = 0;
          if(this.queryParam.quarter=='1'){
            console.log('季1')
            this.jhjgFields.push('一月')
            this.jhjgFields.push('二月')
            this.jhjgFields.push('三月')
          }
          if(this.queryParam.quarter=='2'){
            console.log('季2')
            this.jhjgFields.push('四月')
            this.jhjgFields.push('五月')
            this.jhjgFields.push('六月')
          }
          if(this.queryParam.quarter=='3'){
            console.log('季3')
            this.jhjgFields.push('七月')
            this.jhjgFields.push('八月')
            this.jhjgFields.push('九月')
          }
          if(this.queryParam.quarter=='4'){
            console.log('季4')
            this.jhjgFields.push('十月')
            this.jhjgFields.push('十一月')
            this.jhjgFields.push('十二月')
          }
          return
        }
        if(this.queryParam.month!=undefined&&this.queryParam.month!=''){
          console.log('月')
          this.jhjgFields.length = 0;
          for(let key in map){
            if(key!='type'){
              this.jhjgFields.push(key)
            }
          }
          return
        }
        if(this.queryParam.week!=undefined&&this.queryParam.week!=''){
          console.log('周')
          this.jhjgFields.length = 0;
          for(let key in map){
            if(key!='type'){
              this.jhjgFields.push(key)
            }
          }
        }
        if(this.queryParam.year!=undefined&&this.queryParam.year!=''){
          console.log('年')
          this.jhjgFields.length = 0;
          this.jhjgFields.push('一月')
          this.jhjgFields.push('二月')
          this.jhjgFields.push('三月')
          this.jhjgFields.push('四月')
          this.jhjgFields.push('五月')
          this.jhjgFields.push('六月')
          this.jhjgFields.push('七月')
          this.jhjgFields.push('八月')
          this.jhjgFields.push('九月')
          this.jhjgFields.push('十月')
          this.jhjgFields.push('十一月')
          this.jhjgFields.push('十二月')
          return
        }
      },
      searchQuery() {
        this.loadData()
      },
      // 计划年份
      getCurrentYear() {
        let date = new Date()
        this.year = date.getFullYear()
      },
      particularsShow(record) {
        this.visible = true;
        console.log(record)
        this.valuee = record
        this.getCurrentYear();
        this.searchReset();
      },
      handleOk() {
        this.close()
      },
      // 关闭
      handleCancel() {
        this.close()
      },
      close() {
        this.queryParam.week = undefined,
          this.queryParam.year = undefined,
          this.queryParam.quarter = undefined,
          this.$emit('close')
        this.visible = false
      }
    }
  }
</script>