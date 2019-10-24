<template>
    <div>
        <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :current-page="curPage"
        :total="total"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        ></el-pagination>
    </div>
</template>
<script>
export default {
    name:'page',
    data(){
        return{
            total:50, // 总数
            curPage:4, // 当前页数
            pageSize:20,
            pageSizes:[10,15,20,30,50]
        }
    }
}
</script>

