package ru.practicum.yandex.tasktracker.managers;


public class ManagerSaveException extends RuntimeException
{

    public ManagerSaveException (String message) {
super(message);
    }
    public ManagerSaveException () {
        super();
    }
}
