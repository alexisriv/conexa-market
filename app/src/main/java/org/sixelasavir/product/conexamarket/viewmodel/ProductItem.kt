package org.sixelasavir.product.conexamarket.viewmodel

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xwray.groupie.viewbinding.BindableItem
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.sixelasavir.product.conexamarket.R
import org.sixelasavir.product.conexamarket.databinding.ItemProductBinding
import org.sixelasavir.product.conexamarket.model.Product
import org.sixelasavir.product.conexamarket.ui.OnClickListener

class ProductItem(
    private val product: Product
) : BindableItem<ItemProductBinding>() {

    lateinit var onClickListener: OnClickListener

    override fun bind(viewBinding: ItemProductBinding, position: Int) {
        viewBinding.apply {
            titleTextView.text = product.title
            priceTextView.text = product.price.toString()
            Glide.with(root)
                .load(product.image)
                .optionalCenterInside()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
            addImageButton.setOnClickListener {
                ++(product.count)
                onClickListener.onclick(product)
                countTextView.text = product.count.toString()
            }
            removeImageButton.setOnClickListener {
                if (product.count > 0) {
                    --(product.count)
                    onClickListener.onclick(product)
                    countTextView.text = product.count.toString()
                }
            }
            countTextView.text = product.count.toString()
        }
    }

    override fun getLayout(): Int = R.layout.item_product

    override fun initializeViewBinding(view: View): ItemProductBinding =
        ItemProductBinding.bind(view)
}