package cn.innc11.QuickShopX.utils;

public class Pair <K, V>
{
	public K key;
	public V value;
	
	public K getKey()
	{
		return key;
	}
	
	public V getValue()
	{
		return value;
	}
	
	public Pair(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
}
