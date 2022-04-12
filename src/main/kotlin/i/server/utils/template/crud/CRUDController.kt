package i.server.utils.template.crud

import i.server.handler.inject.page.RestPage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * 通用CRUD Controller
 *
 * @param IN : Any 传入对象
 * @param OUT : Any 传出对象
 * @param ID : Any ID 类型
 * @property service CRUDService<IN, OUT, ID>
 * @constructor
 */
abstract class CRUDController<IN : Any, OUT : CRUDResultView<ID>, ID : Comparable<ID>>(
    private val service: CRUDService<IN, OUT, ID>,
) {
    @Operation(
        description = "分页查询",
        security = [SecurityRequirement(name = "bearer")],
        parameters = [
            Parameter(
                name = "index",
                schema = Schema(implementation = Int::class, defaultValue = "0"),
                `in` = ParameterIn.QUERY,
                example = "0"
            ),
            Parameter(
                name = "size",
                schema = Schema(implementation = Int::class, defaultValue = "10"),
                `in` = ParameterIn.QUERY,
                example = "10"
            )
        ]
    )
    @GetMapping("")
    open fun getAll(@RestPage pageable: Pageable) = service.select(pageable)

    @Operation(
        security = [SecurityRequirement(name = "bearer")]
    )
    @PostMapping("")
    open fun insert(@Valid @RequestBody input: IN) = service.insert(input)

    @Operation(
        security = [SecurityRequirement(name = "bearer")]
    )
    @PutMapping("{id}")
    open fun update(@PathVariable("id") id: ID, @Valid @RequestBody input: IN) = service.update(id, input)

    @Operation(
        security = [SecurityRequirement(name = "bearer")]
    )
    @DeleteMapping("{id}")
    open fun delete(@PathVariable("id") id: ID) = service.delete(id)
}
