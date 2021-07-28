package org.sixelasavir.product.conexamarket.viewmodel

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xwray.groupie.viewbinding.BindableItem
import org.sixelasavir.product.conexamarket.R
import org.sixelasavir.product.conexamarket.databinding.ItemProductBinding
import org.sixelasavir.product.conexamarket.model.Product

class ProductItem(
    private val product: Product
) : BindableItem<ItemProductBinding>() {

    override fun bind(viewBinding: ItemProductBinding, position: Int) {
        viewBinding.apply {
            titleTextView.text = product.title
            priceTextView.text = product.price.toString()
            Glide.with(root)
                .load(product.image)
                .optionalCenterInside()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
        }
    }

    override fun getLayout(): Int = R.layout.item_product

    override fun initializeViewBinding(view: View): ItemProductBinding =
        ItemProductBinding.bind(view)
}