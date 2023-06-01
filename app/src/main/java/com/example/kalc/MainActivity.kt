package com.example.kalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.kalc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var canAddOperator=false
    var canAddDot=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
    }


    fun numberOperate(view: View) {
        if(view is Button){
            if(view.text=="."){
                if(canAddDot==true) {
                    binding.tvExpression.append(view.text)
                    canAddDot = false
                }
            }
            else{
                binding.tvExpression.append(view.text)
                canAddOperator=true
            }

        }
    }
    fun OperatorOperate(view: View) {
        if(view is Button){
            if(canAddOperator) {
                binding.tvExpression.append(view.text)
                canAddOperator=false
                canAddDot=true
            }
        }
    }

    fun AllClear(view: View) {
        binding.tvResult.text=""
        binding.tvExpression.text=""
    }
    fun BackPress(view: View) {
        val length=binding.tvExpression.length()
        if(length>0){
            if(binding.tvExpression.text[length-1] == '.'){
                canAddDot=true
                canAddOperator=true
            }
            else if(!binding.tvExpression.text[length-1].isDigit()){
                canAddOperator=true
            }
            binding.tvExpression.text=binding.tvExpression.text.subSequence(0,length-1)
        }
    }
    fun equalOperate(view: View) {
        val length=binding.tvExpression.length()
        if(binding.tvExpression.text[length-1]=='*' || binding.tvExpression.text[length-1]=='/' || binding.tvExpression.text[length-1]=='.')
            binding.tvResult.text="Error"
        else {
            binding.tvResult.text=calculateAnswer()
        }
    }

    private fun calculateAnswer(): String {
        val list=digitOperation()
        if(list.isEmpty())return ""
        val timedivlist=multiplyDivideCalculate(list)
        if(timedivlist.isEmpty())return ""
        val result=addMultCalculate(timedivlist)
        return result.toString()
    }

    private fun addMultCalculate(list: MutableList<Any>): Float {
        var result=list[0] as Float
        for(i in list.indices){
            if(list[i] is Char && i != list.lastIndex){
                val operator=list[i]
                when(operator){
                    '+'->{
                        result+=(list[i+1] as Float)
                    }
                    '-'->{
                        result-=(list[i+1] as Float)
                    }
                }
            }
        }
        return result
    }

    private fun multiplyDivideCalculate( passedList: MutableList<Any>): MutableList<Any> {

        var list=passedList
        while(list.contains('*') || list.contains('/')){
            list=removeMultDivfromList(list)
        }
        return list
    }

    private fun removeMultDivfromList(list: MutableList<Any>): MutableList<Any> {
        var newList = mutableListOf<Any>()
        var resultIndex=list.size
        for(i in list.indices){
            if(list[i] is Char && i!= list.lastIndex && i < resultIndex ){
                when(list[i]){
                    '*'->{
                        val preVal=list[i-1] as Float
                        val nxtVal=list[i+1] as Float
                        newList.add(preVal*nxtVal)
                        resultIndex=i+1
                    }
                    '/'->{
                        val preVal=list[i-1] as Float
                        val nxtVal=list[i+1] as Float
                        newList.add(preVal/nxtVal)
                        resultIndex=i+1
                    }
                    else -> {
                        newList.add(list[i-1])
                        newList.add(list[i])
                    }
                }
            }
            else if(i > resultIndex)newList.add(list[i])
        }
        return newList
    }

    private fun digitOperation() : MutableList<Any>{
       val list = mutableListOf<Any>()
       var currentDigit=""
       for(character in binding.tvExpression.text){
           if(character.isDigit() || character=='.'){
               currentDigit+=character
           }
           else{
               list.add(currentDigit.toFloat())
               currentDigit=""
               list.add(character)
           }
       }
       if(currentDigit.isNotEmpty())list.add(currentDigit.toFloat())
       return list
   }


}