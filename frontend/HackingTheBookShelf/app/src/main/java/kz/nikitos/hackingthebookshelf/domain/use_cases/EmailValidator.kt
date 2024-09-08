package kz.nikitos.hackingthebookshelf.domain.use_cases

import javax.inject.Inject

class EmailValidator @Inject constructor() {
    operator fun invoke(email: String): Boolean = EMAIL_REGEX.toRegex().matches(email)

    private companion object {
        const val EMAIL_REGEX = """^\w+\@\w+\.\w+$"""

    }
}