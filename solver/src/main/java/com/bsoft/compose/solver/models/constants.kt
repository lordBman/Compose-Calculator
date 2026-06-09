package com.bsoft.compose.solver.models

object Constants{
    const val MINUS_SIGN: Char = '-'
    const val PLUS_SIGN: Char = '+'
    const val MULTIPLICATION_SIGN = '\u00D7'
    const val DIVISION_SIGN = '/'
    const val PERMUTATION = 'P'
    const val COMBINATION = 'C'

    const val OPERATORS = "${MINUS_SIGN}${PLUS_SIGN}${MULTIPLICATION_SIGN}${DIVISION_SIGN}${PERMUTATION}${COMBINATION}"

    const val PI_SYMBOL = '\u03C0'
    const val EXPONENTIAL_SYMBOL = 'e'
    const val FACTORIAL_SYMBOL = '!'
    const val PERCENT_SIGN = '%'
    const val POWER_TWO_SYMBOL = '\u00B2'
    const val POWER_THREE_SYMBOL = '\u00B3'

    const val CONSTANTS = "${PI_SYMBOL}${EXPONENTIAL_SYMBOL}"
    const val MODIFIERS = "${FACTORIAL_SYMBOL}${PERCENT_SIGN}${POWER_TWO_SYMBOL}${POWER_THREE_SYMBOL}"

    const val INVERT_SIGN = "\u00AF\u00B9"
    const val SQUARE_ROOT_SIGN = '\u221A'
}