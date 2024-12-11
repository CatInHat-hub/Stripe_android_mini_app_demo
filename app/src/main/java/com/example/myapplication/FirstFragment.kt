package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.data.GridDataClass
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.recyclerview.CustomRecyclerViewAdapter
import com.example.myapplication.stripeapi.StripePaymentService
import com.example.myapplication.stripeapi.StripeViewModel


class FirstFragment : BaseFragment() {

    val stripeViewModel: StripeViewModel by activityViewModels()
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    lateinit var productList: List<GridDataClass>
    lateinit var customAdapter: CustomRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        MyLog.funcStart()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MyLog.funcStart()
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MyLog.funcStart()
        super.onViewCreated(view, savedInstanceState)
        val mainActivity= activity as MainActivity
        //RecyclerList
        productList=ArrayList()
        productList=productList+GridDataClass("Subscription 1","Des 1213123")
        productList=productList+GridDataClass("Subscription 2","Des 2213123")
        productList=productList+GridDataClass("Subscription 3","Des 3213123")
        productList=productList+GridDataClass("Subscription 4","Des 4213123")
        productList=productList+GridDataClass("Subscription 5","Des 5213123")
        productList=productList+GridDataClass("Subscription 6","Des 6213123")
        productList=productList+GridDataClass("Subscription 7","Des 7213123")
        productList=productList+GridDataClass("Subscription 8","Des 8213123")
        customAdapter = CustomRecyclerViewAdapter(productList)
        val recyclerView: RecyclerView = view.findViewById(R.id.grid_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        recyclerView.adapter=customAdapter
        /*stripeViewModel.productLiveData.observe(viewLifecycleOwner){productResponse->
            productResponse.data.forEach {
                productList=productList+GridDataClass(it.name,it.default_price)
                MyLog.info("product ${it.name}")
            }
            customAdapter.setItems(productList)
        }*/

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.buttonFirst.setOnClickListener {
        }
        binding.buttonPayment.setOnClickListener {
            //stripeViewModel.fetchApiSetupIntent()
            stripeViewModel.fetchApiPaymentIntent(1000,"jpy"," ")
        }
        binding.button3.setOnClickListener {
        }

        stripeViewModel.customerLiveData.observe(viewLifecycleOwner){customer->
            binding.textView2.text="Welcome "+customer.name+".\nYour email:"+customer.email
        }
        stripeViewModel.paymentIntentLiveData.observe(viewLifecycleOwner){paymentIntent->
            if (paymentIntent.clientSecret!=null){
                mainActivity.stripePaymentManager.presentWithPaymentIntent(
                    paymentIntent.clientSecret.toString()
                )
            }
        }
        stripeViewModel.setupIntentLiveData.observe(viewLifecycleOwner){setupIntent->
            if (setupIntent.setupIntentClientSecret!=null){
                mainActivity.stripePaymentManager.presentWithSetupIntent(
                    setupIntent.setupIntentClientSecret.toString()
                )
            }
        }
    }

    override fun onResume() {
        MyLog.funcStart()
        super.onResume()
    }

    override fun onPause() {
        MyLog.funcStart()
        MyLog.info("onPause")
        super.onPause()

    }

    override fun onDestroyView() {
        MyLog.funcStart()
        super.onDestroyView()
        _binding = null
    }

















}