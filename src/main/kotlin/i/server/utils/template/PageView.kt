package i.server.utils.template

data class PageView<T : Any>(
    /**
     * 数据
     */
    val data: List<T>,

    /**
     * 当前页码
     */
    val pageIndex: Int,

    /**
     * 页码总量
     */
    val pageCount: Int,

    /**
     * 数据总数量
     */
    val size: Long,

)
