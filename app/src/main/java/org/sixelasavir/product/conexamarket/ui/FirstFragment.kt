package org.sixelasavir.product.conexamarket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xwray.groupie.GroupieAdapter
import org.sixelasavir.product.conexamarket.R
import org.sixelasavir.product.conexamarket.databinding.FragmentFirstBinding
import org.sixelasavir.product.conexamarket.viewmodel.ProductViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val viewModel: ProductViewModel by activityViewModels()

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var groupieAdapter: GroupieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        viewModel.products.observe(viewLifecycleOwner) {
            groupieAdapter.clear()
            groupieAdapter.addAll(it)
            groupieAdapter.notifyDataSetChanged()
        }
        viewModel.categories.observe(viewLifecycleOwner, ::dialogShow)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.loadProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_shopping_cart)
        menuItem.actionView.also { view ->
            view.setOnClickListener { onOptionsItemSelected(menuItem) }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_shopping_cart -> {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                true
            }
            R.id.action_category -> {
                viewModel.loadProductCategories()
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

    private fun dialogShow(categories: List<String>) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.categories)
            .setItems(
                categories.toTypedArray()
            ) { _, i ->
                viewModel.loadProductsByCategory(categories[i])
            }
            .create()
            .show()
    }
}