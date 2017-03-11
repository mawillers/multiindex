package com.github.mawillers.multiindex;

final class Employee
{
    final int m_id;
    final String m_name;
    final int m_age;
    final String m_city;

    Employee(int id, String name, int age, String city)
    {
        m_id = id;
        m_name = name;
        m_age = age;
        m_city = city;
    }

    @Override
    public String toString()
    {
        return m_id + ":" + m_name + ":" + m_age + ":" + m_city;
    }
}
