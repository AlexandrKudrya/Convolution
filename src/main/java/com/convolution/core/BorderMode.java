package com.convolution.core;

public enum BorderMode {
    ZERO, // заполняем нулями за пределами изображения
    REFLECT, // зеркально отражаем координаты
    CLAMP // зажимаем координаты в допустимый диапазон
}
