package i.server.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

fun <T : Any> autoRollback(statement: Transaction.() -> T): T {
    return transaction {
        try {
            statement()
        } catch (e: Exception) {
            rollback()
            throw e
        }
    }
}

fun <T> Column<T>.comment(comment: String = ""): Column<T> {
    return this
}
