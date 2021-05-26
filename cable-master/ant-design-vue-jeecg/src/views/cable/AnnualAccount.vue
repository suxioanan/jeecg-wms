<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="24">
            <a-form-item label="计划年份">
              <a-select v-model="queryParam.dateTime" placeholder="请选择计划年份" @change="searchQuery">
                <a-select-option :value="year">{{year}}</a-select-option>
                <a-select-option :value="year-1">{{year-1}}</a-select-option>
                <a-select-option :value="year-2">{{year-2}}</a-select-option>
                <a-select-option :value="year-3">{{year-3}}</a-select-option>
                <a-select-option :value="year-4">{{year-4}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="24">
            <a-form-item label="计划类型">
              <a-select v-model="queryParam.planType" placeholder="请选择计划类型" @change="searchQuery">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
                <a-select-option value="电缆">电缆</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="备品">备品</a-select-option>
                <a-select-option value="新品">新品</a-select-option>
                <a-select-option value="抢修">抢修</a-select-option>
                <a-select-option value="临措">临措</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="24">
            <a-form-item label="物料编号">
              <a-input v-model="queryParam.serial" placeholder="请输入物料编号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="24">
            <a-form-item label="物料名称">
              <a-input v-model="queryParam.name" placeholder="请输入物料名称"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="24">
            <a-form-item label="资产编号">
              <a-input v-model="queryParam.assetNo" placeholder="请输入物料编号"></a-input>
            </a-form-item>
          </a-col>

          <!--<a-col :md="4" :sm="24">
            <a-form-item label="项目编号">
              <a-input v-model="queryParam.projectNo"  placeholder="请输入项目编号"></a-input>
            </a-form-item>
          </a-col>-->

          <a-col :md="4" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

          <!--<a-dropdown v-if="selectedRowKeys.length > 0">
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="batchDel">
                <a-icon type="delete"/>
                删除
              </a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px;font-weight: bold;"> 批量操作
              <a-icon type="down"/>
            </a-button>
          </a-dropdown>-->

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      rowKey="id"
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
          <j-ellipsis :value="text" :length="12"/>
        </span>

      </a-table>
    </div>
    <!-- table区域-END -->

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import ACol from 'ant-design-vue/es/grid/Col'
  import JEllipsis from '../../components/jeecg/JEllipsis'

  export default {
    name: 'AnnualAccount',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      ACol,
      AFormItem,
      JEllipsis
    },
    data() {
      return {
        description: '物料年度出入台账',
        // 计划年份
        year: '',
        columns: [
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType',
            scopedSlots: { customRender: 'factoryText' }
          }, {
            title: '物料编号',
            align: 'center',
            dataIndex: 'serial',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'name',
            scopedSlots: { customRender: 'factoryText' }
          },
          // {
          //   title: '资产编号',
          //   align: 'center',
          //   dataIndex: 'assetNo',
          //   scopedSlots: {customRender: 'factoryText'}
          // },
          {
            title: '1月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm11'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm10'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain1'
              }
            ]
          }, {
            title: '2月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm21'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm20'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain2'
              }
            ]
          }, {
            title: '3月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm31'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm30'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain3'
              }
            ]
          }, {
            title: '4月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm41'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm40'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain4'
              }
            ]
          }, {
            title: '5月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm51'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm50'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain5'
              }
            ]
          }, {
            title: '6月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm61'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm60'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain6'
              }
            ]
          }, {
            title: '7月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm71'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm70'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain7'
              }
            ]
          }, {
            title: '8月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm81'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm80'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain8'
              }
            ]
          }, {
            title: '9月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm91'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm90'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain9'
              }
            ]
          }, {
            title: '10月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm101'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm100'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain10'
              }
            ]
          }, {
            title: '11月',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'm111'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm110'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain11'
              }
            ]
          }, {
            title: '12月',
            align: 'center',
            children: [
              {
                // StorageLocationList
                title: '入库',
                align: 'center',
                dataIndex: 'm121'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'm120'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'remain12'
              }
            ]
          }, {
            title: '合计',
            align: 'center',
            children: [
              {
                title: '入库',
                align: 'center',
                dataIndex: 'mh1'
              }, {
                title: '出库',
                align: 'center',
                dataIndex: 'mh0'
              }, {
                title: '剩余',
                align: 'center',
                dataIndex: 'mhRemain'
              }
            ]
          }
        ],
        url: {
            list: '/cable/material/getAnnualAccountList'
        }
      }
    },
    created() {
      // 调用计划年份
      this.getCurrentYear();
    },
    methods: {
      // 计划年份
      getCurrentYear() {
        let date = new Date();
        this.year = date.getFullYear();
        this.queryParam.dateTime = this.year
        console.log("当前年份", this.year)
      }
    }
  }
</script>

<style scoped>

</style>