<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="计划类型">
              <a-select
                v-model="queryParam.planType"
                placeholder="请选择计划类型">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="电缆">电缆</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
                <a-select-option value="备品">备品</a-select-option>
                <a-select-option value="新品">新品</a-select-option>
                <a-select-option value="抢修">抢修</a-select-option>
                <a-select-option value="临措">临措</a-select-option>
                <a-select-option value="电缆2">电缆2</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="结算状态">
              <a-select
                v-model="queryParam.backup1"
                placeholder="请选择结算状态">
                <a-select-option value="0">未结算</a-select-option>
                <a-select-option value="1">已结算</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="项目编号">
              <a-input placeholder="请输入项目编号" v-model="queryParam.projectNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="7" :sm="10">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

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
          <j-ellipsis :value="text" :length="10"/>
        </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdite(record)">详情</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定结算吗?" @confirm="() =>handleEdits(record)">
            <a>结算</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <!-- 计算详情页面 modal -->
    <settle-accounts-details-modal ref="SettleAccountsDetailsModal"></settle-accounts-details-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { putAction,getAction } from '@/api/manage'
  import SettleAccountsDetailsModal from './modules/SettleAccountsDetailsModal'
  import JEllipsis from '../../components/jeecg/JEllipsis'

  export default {
    name: 'SettleAccounts',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      SettleAccountsDetailsModal,
      JEllipsis
    },
    data() {
      return {
        description: '计划结算页面',
        form: this.$form.createForm(this),
        width: 800,
        loading: false,
        visible: false,
        //查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType'
          },
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo'
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          },
          /*{
            title: '联系人',
            align: 'center',
            dataIndex: 'contact'
          },
          {
            title: '联系电话',
            align: 'center',
            dataIndex: 'phone'
          },
          {
            title: '工程地址',
            align: 'center',
            dataIndex: 'address',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '资产编号',
            align: 'center',
            dataIndex: 'assetNo'
          },*/
          {
            title: '完成状态',
            align: 'center',
            dataIndex: 'completeState_dictText'
          },
          {
            title: '结算状态',
            align: 'center',
            dataIndex: 'backup1_dictText'
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
        url: {
          list: '/cable/plan1/selectSettleAccounts'
        }
      }
    },
    computed: {},
    methods: {
      handleEdite(record) {
        this.$refs.SettleAccountsDetailsModal.showDetail(record)
      },
      handleEdits(record) {
        console.log('计划结算>>>>>>>>>>>>>>>>', record)

        getAction('/cable/plan1/updateSettleAccounts', { projectNo: record.projectNo }).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.loadData(1)
          }
        })
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        var params = this.getQueryParams()//查询条件
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      }
    },
    created() {
      this.loadData(1)
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>