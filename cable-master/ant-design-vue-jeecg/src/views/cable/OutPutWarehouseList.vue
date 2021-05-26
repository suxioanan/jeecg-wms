<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="6">
            <a-form-item label="操作类型">
              <a-select v-model="queryParam.operatorSchema" placeholder="请选择操作类型" @change="searchQuery">
                <a-select-option v-for="(item,index) in operatorSchema" :value="item.itemValue">{{item.itemText}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item label="操作时间">
              <j-date v-model="queryParam.startTime" placeholder="开始时间" style="width: 40%"></j-date>
              <span>——</span>
              <j-date v-model="queryParam.endTime" placeholder="结束时间"style="width: 40%"></j-date>
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
          <!--<a-col :md="4" :sm="6">
            <a-form-item label="计划类型">
              <a-input v-model="queryParam.planType" placeholder="模糊搜索计划类型"></a-input>
            </a-form-item>
          </a-col>-->

          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="6">
              <a-form-item label="项目编号">
                <a-input v-model="queryParam.projectNo" placeholder="精准查询项目编号"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="6">
              <a-form-item label="项目名称">
                <a-input v-model="queryParam.projectName" placeholder="模糊搜索项目名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="6">
              <a-form-item label="物料名称">
                <a-input v-model="queryParam.name" placeholder="模糊搜索物料名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="6">
              <a-form-item label="采购订单号">
                <a-input v-model="queryParam.proTheorderNo" placeholder="模糊搜索采购订单号"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="6">
              <a-form-item label="电力仓库交接单号">
                <a-input v-model="queryParam.receiptNo" placeholder="模糊搜索交接单号"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="7" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">
        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>
        <!-- <template slot="htmlSlot" slot-scope="text">
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
          <a @click="handleEdit(record)">查看库位</a>
        </span>-->
      </a-table>
    </div>
  </a-card>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import JDate from '@/components/jeecg/JDate'
  import { getAction } from '../../api/manage'

  export default {
    name: 'OutPutWarehouseList',
    mixins: [JeecgListMixin, mixinDevice],
    components: { JEllipsis, JDate },
    data () {
      return {
        description: '出车入库页面',
        // 查询条件
        queryParam: {},
        columns: [
          {
            title: '操作类型',
            align: 'center',
            dataIndex: 'operatorSchema_dictText',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '操作时间',
            align: 'center',
            dataIndex: 'taskTime',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
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
          {
            title: '资产编号',
            align: 'center',
            dataIndex: 'assetNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '采购订单号',
            align: 'center',
            dataIndex: 'proTheorderNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '操作数量',
            align: 'center',
            dataIndex: 'accomplishNumConcatUnit',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '操作重量',
            align: 'center',
            dataIndex: 'accomplishWeightConcatUnit',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '仓库',
            align: 'center',
            dataIndex: 'warehouseName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '终点仓',
            align: 'center',
            dataIndex: 'endWarehouseName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '交接单号',
            align: 'center',
            dataIndex: 'receiptNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '情况说明',
            align: 'center',
            dataIndex: 'annotation',
            scopedSlots: { customRender: 'factoryText' }
          }
          /*{
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: 147,
            scopedSlots: { customRender: 'action' }
          }*/
        ],
        url: {
          list: '/cable/testdata/getOutPutWarehouseList',
        },
        // 操作类型
        operatorSchema: [],
        // 加载路由参数方法类型
        loadRouteType: false
      }
    },
    created () {
      this.getOperatorSchemaList()
      this.loadData(1)
    },
    methods: {
      // 接收首页备品统计 echart 点击事件的参数
      getIndexBPTJdata() {
        let _this = this;
        // 赋值操作
        _this.queryParam.startTime = _this.$route.query.beginTime;
        _this.queryParam.endTime = _this.$route.query.endTime;
        _this.queryParam.planType = _this.$route.query.planType;
        console.log("_this.queryParam.startTime", _this.queryParam.startTime);
        console.log("_this.queryParam.endTime", _this.queryParam.endTime);
        console.log("_this.queryParam.planType", _this.queryParam.planType);
      },
      loadData(arg) {
        if (this.loadRouteType == false) {
          this.getIndexBPTJdata()
          this.loadRouteType = true
        }
        //清空勾选
        this.selectedRowKeys = []
        if(!this.url.list){
          //this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      /**
       * 查询字典操作类型
       */
      getOperatorSchemaList () {
        let that = this
        getAction('/sys/dictItem/getSysDictItemCommon', { 'commonText': 'operatorSchema' }).then(res => {
          if (res.success) {
            console.log(res.result)
            that.operatorSchema = res.result
          }
        })
      }
    }
  }
</script>
<style scoped>
</style>