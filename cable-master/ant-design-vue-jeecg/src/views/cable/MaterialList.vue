<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="物料编号">
              <a-input v-model="queryParam.serial" placeholder="请输入物料编号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料名称">
              <a-input v-model="queryParam.name" placeholder="请输入物料名称"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="供应商名">
              <a-input v-model="queryParam.supplier" placeholder="请输入供应商名"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" icon="plus" style="margin-left: 8px">新增物料</a-button>
              <a-upload
                name="file"
                :showUploadList="false"
                :multiple="false"
                :headers="tokenHeader"
                :action="importExcelUrl"
                @change="handleImportExcel"
                style="margin-left: 8px">
                <a-button type="primary" icon="import">物料导入</a-button>
              </a-upload>
            </span>
          </a-col>

          <!--          <a-dropdown v-if="selectedRowKeys.length > 0">-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item key="1" @click="batchDel">-->
          <!--                <a-icon type="delete"/>-->
          <!--                删除-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--            <a-button style="margin-left: 8px;font-weight: bold;"> 批量操作-->
          <!--              <a-icon type="down"/>-->
          <!--            </a-button>-->
          <!--          </a-dropdown>-->

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

          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical"/>
          <a @click="handleEdit(record)">编辑</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-END -->

    <material-modal ref="modalForm" @ok="modalFormOk"></material-modal>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MaterialModal from './modules/MaterialModal'

  export default {
    name: 'MaterialList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      MaterialModal
    },
    data() {
      return {
        description: '物料管理',
        // 查询条件
        queryParam: {},
        dataSource: [],
        // 表头
        columns: [
          {
            title: '物料编号',
            align: 'center',
            dataIndex: 'serial'
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '物料简称',
            align: 'center',
            dataIndex: 'backup1'
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'unit_dictText'
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
          list: '/cable/material/list',
          delete: '/cable/material/delete',
          deleteBatch: '/cable/material/deleteBatch',
          exportXlsUrl: '/cable/material/exportXls',
          importExcelUrl: '/cable/material/importExcel'
        }
      }
    },
    computed: {
      // 导入操作
      importExcelUrl() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}/`
      }
    },
    method: {
      /* 导入后操作 */
      handleImportExcel(info) {
        if (info.file.status !== 'uploading') {
          console.log(info.file, info.fileList)
        }
        if (info.file.status === 'done') {
          if (info.file.response.success) {
            // this.$message.success(`${info.file.name} 文件上传成功`);
            if (info.file.response.code === 201) {
              let { message, result: { msg, fileUrl, fileName } } = info.file.response
              let href = window._CONFIG['domianURL'] + fileUrl
              this.$warning({
                  title: message,
                  content: (
                    < div >
                    < span > { msg } < /span><br/ >
                    < span > 具体详情请 < a href = { href } target = '_blank' download = { fileName } > 点击下载 < /a> </span >
                < /div>
              )
            })
            } else {
              this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
            }
            this.loadData()
          } else {
            this.$message.error(`${info.file.name} ${info.file.response.message}.`)
          }
        } else if (info.file.status === 'error') {
          this.$message.error(`文件上传失败: ${info.file.msg} `)
        }
      }
    }
  }
</script>

<style scoped>
</style>