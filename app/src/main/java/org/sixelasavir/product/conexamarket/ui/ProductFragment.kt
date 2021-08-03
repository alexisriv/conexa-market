package org.sixelasavir.product.conexamarket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xwray.groupie.GroupieAdapter
import org.sixelasavir.product.conexamarket.R
import org.sixelasavir.product.conexamarket.databinding.FragmentProductBinding
import org.sixelasavir.product.conexamarket.extensible.getListIsNotEmpty
import org.sixelasavir.product.conexamarket.model.Product
import org.sixelasavir.product.conexamarket.utils.EventObserver
import org.sixelasavir.product.conexamarket.viewmodel.ProductItem
import org.sixelasavir.product.conexamarket.viewmodel.ProductViewModel

class ProductFragment : Fragment(), OnClickListener {

    private val viewModel: ProductViewModel by activityViewModels()

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var groupieAdapter: GroupieAdapter

    private lateinit var badgeTextView: TextView
    private lateinit var badgeCartView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        viewModel.apply {
            products.observe(
                viewLifecycleOwner,
                EventObserver {
                    loadAdapter(productItems = it.map { pI0 ->
                        pI0.apply { onClickListener = this@ProductFragment }
                    })
                })
            categories.observe(
                viewLifecycleOwner,
                EventObserver { dialogShow(categories = it) })
            shoppingCart.observe(
                viewLifecycleOwner,
                EventObserver { shoppingCart(count = it) })
        }
        return binding.root
    }

    private fun shoppingCart(count: Long) {
        if (count > 0) {
            badgeTextView.text = count.toString()
            badgeCartView.visibility = VISIBLE
        } else {
            badgeCartView.visibility = INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.products.value?.peekContent()?.getListIsNotEmpty()?.let {
            loadAdapter(it)
        } ?: run {
            viewModel.loadProducts()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_shopping_cart)
        badgeTextView = menuItem.actionView.findViewById(R.id.badgeCountTextView)
        badgeCartView = menuItem.actionView.findViewById(R.id.badgeCardView)
        menuItem.actionView.also { view ->
            view.setOnClickListener { onOptionsItemSelected(menuItem) }
        }
        viewModel.loadShoppingCart()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_shopping_cart -> {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                true
            }
            R.id.action_category -> {
                viewModel.categories.value?.peekContent()?.getListIsNotEmpty()?.let {
                    dialogShow(it)
                } ?: run { viewModel.loadProductCategories() }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val verticalDecorator =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val horizontalDecorator =
            DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)

        ResourcesCompat.getDrawable(
            requireActivity().resources,
            R.drawable.shape_divider,
            null
        )?.also {
            verticalDecorator.setDrawable(it)
            horizontalDecorator.setDrawable(it)
        }

        groupieAdapter = GroupieAdapter().apply {
            spanCount = 2
        }

        binding.productsRecyclerView.apply {
            addItemDecoration(verticalDecorator)
            addItemDecoration(horizontalDecorator)

            layoutManager = StaggeredGridLayoutManager(groupieAdapter.spanCount, 1)
            adapter = groupieAdapter
        }
    }

    private fun loadAdapter(productItems: List<ProductItem>) {
        groupieAdapter.apply {
            clear()
            addAll(productItems)
        }.notifyDataSetChanged()
    }

    private fun dialogShow(categories: List<String>) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.categories)
            .setItems(categories.toTypedArray()) { _, i ->
                viewModel.loadProductsByCategory(categories[i])
            }.create()
            .show()
    }

    override fun onclick(p0: Product) {
        viewModel.saveProductInShoppingCart(p0)
    }
}

interface OnClickListener {
    fun onclick(p0: Product)
}