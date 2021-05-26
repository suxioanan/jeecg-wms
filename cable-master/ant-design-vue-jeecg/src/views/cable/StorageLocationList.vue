<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="storageLocationList">
        <a-row :gutter="24">

          <a-col :md="4" :sm="5">
            <a-form-item label="仓库名称">
              <a-select v-model="queryParam.warehouseId" placeholder="请选择仓库名称" @change="storageLocationList">
                <template v-for="warehouse in warehouseLists">
                  <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="5">
            <a-form-item label="库位名称">
              <a-input placeholder="请输入库位名称" v-model="queryParam.storageLocationName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="5">
            <a-form-item label="库位容积">
              <a-input placeholder="请输入库位容积" v-model="queryParam.storageLocationVolume"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="5" :sm="7">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="storageLocationList" icon="search">查询</a-button>
              <a-button type="primary" @click="searchs" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" style="margin-left: 8px" icon="plus">新增库位</a-button>
            </span>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <div style="display: inline-block;margin-bottom: 50px;margin-left: 20px;">
            <span class="spa" style="background-color: white;border: 1px solid #777777">空闲</span>
            <span class="spa" style="background-color: dodgerblue;margin-left: 10px;color: white">正常</span>
            <span class="spa" style="background-color: orange;margin-left: 10px;color: white">饱和</span>
            <span class="spa" style="background-color: red;margin-left: 10px;color: white">超标</span>
          </div>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 库位展示区域 -->
    <div>
      <span v-for="(storageLocation,index) in storageLocations">
        <!-- 电缆库位 storageLocationName  -->
          <span v-if="storageLocation.storageLocationName == '电缆库位'">
            <div :id="storageLocation.id" class="stordiv">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                 库存:
                 {{storageLocation.percentage}}
               </span>
             </div>
            <div class="aclick">
              <a class="a1" @click="showLocationQRCode(storageLocation.id)">库位二维码</a>
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定要删除库位吗？" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>

        <span v-if="storageLocation.storageLocationName != '电缆库位'">
        <!-- 空闲库位容量 0 为空闲状态-->
          <span v-if="storageLocation.percentage == 0 || storageLocation.percentage < 0">
            <div :id="storageLocation.id" class="stordiv">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                 {{storageLocation.percentage + '%'}}
               </span>
             </div>
            <div class="aclick">
              <a class="a1" @click="showLocationQRCode(storageLocation.id)">库位二维码</a>
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定要删除库位吗？" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>
          </span>

        <span v-if="storageLocation.storageLocationName != '电缆库位'">
        <!-- 正常库位容量 60 至 80 之间为正常状态-->
          <span v-if="storageLocation.percentage > 0 && storageLocation.percentage <= 60">
            <div :id="storageLocation.id" class="stordiv" style="background-color: dodgerblue">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                {{storageLocation.percentage + '%'}}
               </span>
             </div>
            <div class="aclick">
              <a class="a1" @click="showLocationQRCode(storageLocation.id)">库位二维码</a>
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定要删除库位吗？" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>
          </span>

        <span v-if="storageLocation.storageLocationName != '电缆库位'">
        <!-- 饱和库位容量 80 至 100 之间为饱和状态-->
          <span v-if="storageLocation.percentage > 60 && storageLocation.percentage <= 80">
            <div :id="storageLocation.id" class="stordiv" style="background-color: orange">
             <div class="stor">
               {{storageLocation.storageLocationName}}
               <span class="spaa">
                 {{storageLocation.percentage + '%'}}
               </span>
             </div>
            <div class="aclick">
              <a class="a1" @click="showLocationQRCode(storageLocation.id)">库位二维码</a>
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定要删除库位吗？" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>
          </span>

        <span v-if="storageLocation.storageLocationName != '电缆库位'">
        <!-- 满仓库位容量 大于或等于100为满仓状态-->
          <span v-if=" storageLocation.percentage > 80">
            <div :id="storageLocation.id" class="stordiv" style="background-color: red">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                 {{storageLocation.percentage + '%'}}
               </span>
             </div>
            <div class="aclick">
              <a class="a1" @click="showLocationQRCode(storageLocation.id)">库位二维码</a>
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定要删除库位吗？" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>
          </span>

        </span>
    </div>
    <!-- 库位展示区域-END -->

    <storageLocation-modal ref="storageLocationModal" @ok="modalFormOk"></storageLocation-modal>
    <storageLocationAdd-modal ref="storageLocationAddModal" @ok="modalFormOk"></storageLocationAdd-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import StorageLocationModal from './modules/StorageLocationModal'
  import StorageLocationAddModal from './modules/StorageLocationAddModal'
  import {deleteAction, getAction} from '@/api/manage'
  import JInput from '@/components/jeecg/JInput'

  export default {
    name: 'StorageLocationList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      StorageLocationModal,
      StorageLocationAddModal,
      JInput
    },
    data() {
      return {
        description: '库位表管理页面',
        //查询条件
        queryParam: {
          storageLocationName: '',
          storageLocationVolume: ''
        },
        //保存自家仓库
        warehouseLists: {},
        //保存查询出来的库位
        storageLocations: [],
        dictOptions: {},
        url: {
          list: '/cable/storageLocation/list',
          delete: '/cable/storageLocation/delete',
          deleteBatch: '/cable/storageLocation/deleteBatch',
          exportXlsUrl: '/cable/storageLocation/exportXls',
          importExcelUrl: 'cable/storageLocation/importExcel',
          warehouseOneselfList: '/cable/warehouse/warehouseOneselfList'
        },
        wId: ''
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      /**
       * 查看库位二维码
       * @param locationId 库位 ID
       */
      showLocationQRCode(locationId) {
        console.log('库位 iD >>>>>>>>>>>', locationId)
        let url = '/cable/storageLocation/showLocationQRCode'
        getAction(url, {'locationId': locationId}).then((res) => {
          if (res.success) {
            // 二维码图片预览模式
            console.log('二维码图片路径>>>>>>>>', window._CONFIG['domianURL'] + '/' + res.message)
            window.open(window._CONFIG['domianURL'] + '/' + res.message)
          }
        })
      },
      modalFormOk() {
        this.searchs()
      },
      handleAdd() {
        let wa = this.queryParam.warehouseId
        console.log(wa)
        if (wa != undefined) {
          this.$refs.storageLocationAddModal.addStorageLocation(wa)
          this.$refs.storageLocationAddModal.title = ''
        } else {
          window.confirm('请先选择一个仓库!')
        }
      },
      searchs() {
        this.queryParam.storageLocationName = ''
        this.queryParam.storageLocationVolume = ''
        this.storageLocationList()
      },
      hDelete(val) {
        let that = this
        deleteAction(this.url.delete, {id: val}).then((res) => {
          if (res.success) {
            that.storageLocationList()
            this.$message.success(res.message)
          }else{
            this.$message.warning(res.message)
          }
        })
      },
      viewDetails(val) {
        let wa = this.queryParam.warehouseId
        console.log('查看存储物料的库位 id 为val', val)
        this.$refs.storageLocationModal.showStorageLocation(wa, val)
        this.$refs.storageLocationModal.title = ''
      },
      initDictConfig() {
      },
      warehouseList() {
        this.warehouseLists = ''
        getAction(this.url.warehouseOneselfList).then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
            this.queryParam.warehouseId = this.warehouseLists[0].id
            this.storageLocationList()
          }
        })
      },
      storageLocationList() {
        this.wId = this.queryParam.warehouseId
        this.storageLocations = ''
        getAction(this.url.list, {
          warehouseId: this.queryParam.warehouseId,
          storageLocationName: this.queryParam.storageLocationName,
          storageLocationVolume: this.queryParam.storageLocationVolume
        }).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
          }
        })
      }
    },
    created() {
      this.warehouseList()
      this.storageLocationList()
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .stordiv {
    width: 300px;
    height: 100px;
    margin: 5px;
    display: inline-block;
    border: 1px solid #777777;
    border-radius: 5px;
  }

  .stor {
    display: inline-block;
    width: 100%;
    padding-top: 10px;
    padding-left: 10px;
  }

  .spa {
    display: inline-block;
    width: 80px;
    height: 33px;
    line-height: 33px;
    text-align: center;
    border-radius: 5px
  }

  .aclick {
    float: right;
    margin-top: 20px;
  }

  .a1 {
    display: inline-block;
    color: white;
    width: 95px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    border-radius: 5px;
    background-color: darkcyan;
    margin-right: 5px;
  }

  .a2 {
    display: inline-block;
    color: white;
    width: 30px;
    height: 30px;
    line-height: 30px;
    background-color: darkcyan;
    text-align: center;
    margin-left: 5px;
    border-radius: 5px;
    margin-right: 10px;
  }

  .spaa {
    margin-right: 30px;
    float: right;
    display: inline-block;
    font-size: 16px;
  }

  .yid {
    display: inline-block;
    float: left;
  }

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