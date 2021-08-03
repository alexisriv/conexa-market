package org.sixelasavir.product.conexamarket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.xwray.groupie.GroupieAdapter
import org.sixelasavir.product.conexamarket.R
import org.sixelasavir.product.conexamarket.dao.Cart
import org.sixelasavir.product.conexamarket.databinding.FragmentShoppingCartBinding
import org.sixelasavir.product.conexamarket.extensible.getListIsNotEmpty
import org.sixelasavir.product.conexamarket.utils.EventObserver
import org.sixelasavir.product.conexamarket.viewmodel.ItemShoppingCart
import org.sixelasavir.product.conexamarket.viewmodel.ShoppingCartViewModel

class ShoppingCartFragment : Fragment() {

    private val viewModel: ShoppingCartViewModel by activityViewModels()

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var groupieAdapter: GroupieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        viewModel.apply {
            items.observe(viewLifecycleOwner, EventObserver(::loadAdapter))
            cart.observe(viewLifecycleOwner, EventObserver(::loadBottom))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.items.value?.peekContent()?.getListIsNotEmpty()?.let {
            loadAdapter(it)
        } ?: run {
            viewModel.loadShoppingCartItems()
        }
        viewModel.cart.value?.peekContent()?.let {
            loadBottom(it)
        } ?: run {
            viewModel.loadCart()
        }
    }

    private fun setupRecyclerView() {
        val verticalDecorator =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ResourcesCompat.getDrawable(
            requireActivity().resources,
            R.drawable.shape_divider_2,
            null
        )?.also {
            verticalDecorator.setDrawable(it)
        }

        groupieAdapter = GroupieAdapter()
        binding.shoppingCartRecyclerView.apply {
            addItemDecoration(verticalDecorator)

            layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
            adapter = groupieAdapter
        }
    }

    private fun loadBottom(cart: Cart) {
        binding.amountTextView.text =
            requireContext().getString(R.string.amount, "%.2f".format(cart.total))
    }

    private fun loadAdapter(items: List<ItemShoppingCart>) {
        groupieAdapter.apply {
            clear()
            addAll(items)
        }.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
