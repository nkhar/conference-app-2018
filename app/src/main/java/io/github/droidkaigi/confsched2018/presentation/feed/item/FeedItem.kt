package io.github.droidkaigi.confsched2018.presentation.feed.item

import android.support.constraint.ConstraintSet
import android.support.transition.Transition
import android.support.transition.TransitionListenerAdapter
import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemFeedBinding
import io.github.droidkaigi.confsched2018.model.Post

data class FeedItem(
        val post: Post,
        private val feedItemCollapsed: ConstraintSet,
        private val feedItemExpanded: ConstraintSet,
        private val expandTransition: Transition,
        private val collapseTransition: Transition,
        private var expanded: Boolean = false
) : BindableItem<ItemFeedBinding>(
        post.hashCode().toLong()
) {
    private val touchIgnorer = View.OnTouchListener { _, _ -> true }

    override fun bind(viewBinding: ItemFeedBinding, position: Int) {
        viewBinding.post = post
        viewBinding.root.setOnClickListener {
            val parent = viewBinding.root.parent as RecyclerView
            parent.setOnTouchListener(touchIgnorer)
            val transition = (if (expanded) collapseTransition else expandTransition).clone()

            transition.addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    parent.setOnTouchListener(null)
                }
            })
            TransitionManager.beginDelayedTransition(parent, transition)
            expanded = !expanded
            if (!expanded) {
                feedItemCollapsed.applyTo(viewBinding.feedItemConstraintLayout)
            } else {
                feedItemExpanded.applyTo(viewBinding.feedItemConstraintLayout)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_feed
}
