package ru.ruscalworld.universalskins.util;

// Более понятное (скорее всего) название - Callback
// Используется для получения значений из методов, выполняемых асинхронно
public abstract class ResultHandler<T> {
    // Должно вызываться, когда метод выполнен
    // В качестве единственного аргумента передаётся возвращаемое значение асинхронного метода
    public abstract void handle(T result);
}
