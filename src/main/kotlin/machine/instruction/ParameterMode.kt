package machine.instruction

enum class ParameterMode(val raw: Int) {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2);

    companion object {
        fun fromRaw(raw: Int) = ParameterMode.values().firstOrNull { it.raw == raw }
    }
}