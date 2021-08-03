package org.sixelasavir.product.conexamarket.viewmodel

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.DATA
import com.xwray.groupie.viewbinding.BindableItem
import org.sixelasavir.product.conexamarket.R
import org.sixelasavir.product.conexamarket.databinding.ItemShoppingCartBinding
import org.sixelasavir.product.conexamarket.model.entity.ItemCart

class ItemShoppingCart(
    private val item: ItemCart
) : BindableItem<ItemShoppingCartBinding>() {

    private val float2 = "%.2f"

    override fun bind(viewBinding: ItemShoppingCartBinding, position: Int) {
        viewBinding.apply {
            titleTextView.text = item.title

            val subtotal = item.run { price * count }
            countSubtitle.text = root.context.getString(
                R.string.title_count,

                float2.format(item.price),
                item.count.toString(),
                float2.format(subtotal)
            )
            Glide.with(root)
                .load(item.image)
                .optionalCircleCrop()
                .diskCacheStrategy(DATA)
                .into(itemImageView)
        }
    }

    override fun getLayout(): Int = R.layout.item_shopping_cart

    override fun initializeViewBinding(view: View): ItemShoppingCartBinding =
        ItemShoppingCartBinding.bind(view)
}
