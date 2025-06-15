# CONVOLUTION
## О проекте

Этот проект содержит набор последовательных и параллельных реализаций двумерной свёртки изображений на Java. 


## Быстрый старт

```sh
./gradlew build
```

---

## Сравнение реализаций свёртки

### **Реализации**

* `SequentialConvolution` — последовательная
* `ParallelRowConvolution` — параллелизм по строкам
* `ParallelColumnConvolution` — по столбцам
* `ParallelPixelConvolution` — по пикселям
* `TiledConvolution` — по плиткам (тайлы 16×16, 32×32, 128×128)

### **Методика**

* Размеры изображений: **64×64**, **1024×1024**
* Размеры ядер: **3×3**, **9×9**
* Замеры: JMH, 3 повтора

#### **Результаты JMH**

#### 64 × 64, 3 × 3

| Реализация          | Score (ms/op) | Error (ms) |
|---------------------|--------------:|-----------:|
| seqConvolution      | 0.086         | 0.002      |
| parColConvolution   | 0.042         | 0.002      |
| parPixConvolution   | 0.044         | 0.001      |
| parRowConvolution   | 0.043         | 0.002      |
| tiled128Convolution | 0.095         | 0.001      |
| tiled16Convolution  | 0.059         | 0.001      |
| tiled32Convolution  | 0.058         | 0.003      |

#### 64 × 64, 9 × 9

| Реализация          | Score (ms/op) | Error (ms) |
|---------------------|--------------:|-----------:|
| seqConvolution      | 0.444         | 0.006      |
| parColConvolution   | 0.126         | 0.002      |
| parPixConvolution   | 0.125         | 0.005      |
| parRowConvolution   | 0.127         | 0.008      |
| tiled128Convolution | 0.481         | 0.020      |
| tiled16Convolution  | 0.134         | 0.001      |
| tiled32Convolution  | 0.224         | 0.018      |

#### 1024 × 1024, 3 × 3

| Реализация          | Score (ms/op) | Error (ms) |
|---------------------|--------------:|-----------:|
| seqConvolution      | 21.190        | 0.210      |
| parColConvolution   | 4.655         | 0.285      |
| parPixConvolution   | 4.569         | 0.139      |
| parRowConvolution   | 4.383         | 0.098      |
| tiled128Convolution | 4.294         | 0.188      |
| tiled16Convolution  | 4.377         | 0.078      |
| tiled32Convolution  | 4.360         | 0.274      |

#### 1024 × 1024, 9 × 9

| Реализация          | Score (ms/op) | Error (ms) |
|---------------------|--------------:|-----------:|
| seqConvolution      | 111.638       | 1.387      |
| parColConvolution   | 22.732        | 0.501      |
| parPixConvolution   | 21.217        | 0.546      |
| parRowConvolution   | 21.002        | 0.738      |
| tiled128Convolution | 20.634        | 0.677      |
| tiled16Convolution  | 23.702        | 2.083      |
| tiled32Convolution  | 20.943        | 0.885      |

## Как воспроизвести
Сборка и запуск микробенчмарков:
   ```sh
   ./gradlew build
   ./gradlew jmh
   ```
Результаты в консоли и в `build/reports/jmh/`

---

## Пакетная обработка

Был реализован пайплан для пакетной обработки данных. 

**Сравнение скорости обработки датасета (асинхронно и последовательно):**

Замеры производились на [датасете](https://www.kaggle.com/datasets/pavansanagapati/images-dataset/data). В таблице представленно время полной обработки всех 3600+ изображений фильтром 3 на 3. 

| Реализация                 | Асинхронный (сек) | Последовательный (сек) |
| -------------------------- | ----------------- | ---------------------- |
| SequentialConvolution      | 10,08             | 46,35                  |
| ParallelPixelConvolution   | 12,01             | 28,46                  |
| ParallelColumnConvolution  | 15,26             | 24,83                  |
| ParallelRowConvolution     | 12,74             | 26,12                  |
| TiledConvolution (128x128) | 12,92             | 28,80                  |
| TiledConvolution (32x32)   | 10,89             | 26,25                  |
| TiledConvolution (16x16)   | 9,81              | 27,67                  |

---

## Структура проекта

* `/src/main/java/com/convolution/core/` — базовые и параллельные реализации свёртки
* `/src/main/java/com/convolution/batch/` — пайплайны, batch-обработка
* `/src/test/` — property-based и юнит-тесты

---

## Используемые инструменты

- **Java 21** — основной язык реализации
- **Gradle** — система сборки и управления зависимостями
- **BoofCV** — библиотека для работы с изображениями (GrayU8 и др.)
- **JMH (Java Microbenchmark Harness)** — микробенчмарки
- **jqwik** — property-based тестирование
- **JUnit** — unit-тесты